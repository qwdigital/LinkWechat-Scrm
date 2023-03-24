package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.enums.TaskFissionType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.fission.*;
import com.linkwechat.domain.fission.vo.*;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.groupmsg.vo.WeGroupMessageExecuteUsertipVo;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatAddJoinWayVo;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.*;
import com.linkwechat.mapper.WeFissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author robin
* @description 针对表【we_fission(裂变（任务宝,群裂变）)】的数据库操作Service实现
* @createDate 2023-03-14 14:07:21
*/
@Service
public class WeFissionServiceImpl extends ServiceImpl<WeFissionMapper, WeFission>
    implements IWeFissionService {

    @Autowired
    private IWeFissionInviterPosterService iWeFissionInviterPosterService;

    @Autowired
    private IWeFissionInviterRecordService iWeFissionInviterRecordService;

    @Autowired
    private IWeFissionInviterRecordSubService iWeFissionInviterRecordSubService;

    @Autowired
    private IWeFissionNoticeService iWeFissionNoticeService;

    @Autowired
    private IWeCustomerService iWeCustomerService;


    @Autowired
    private IWeMessagePushService iWeMessagePushService;


    @Autowired
    private IWeGroupCodeService iWeGroupCodeService;

    @Autowired
    private IWeGroupService iWeGroupService;

    @Autowired
    private QwSysUserClient qwSysUserClient;


    @Autowired
    private IWeQrCodeService iWeQrCodeService;


    @Autowired
    private IWeMaterialService materialService;


    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @Override
    @Transactional
    public void buildWeFission(WeFission weFission) {

        if(weFission.getId()==null){
            weFission.setId(SnowFlakeUtil.nextId());
            //裂变h5链接
            weFission.setFissionUrl(
                    MessageFormat.format(linkWeChatConfig.getFissionUrl(), weFission.getId().toString())
            );
        }


        //设置活动状态
        if(DateUtils.parseDate(weFission.getBeginTime())//设置为进行中
                .before(new Date())){
            weFission.setFassionState(2);
        }


        if(this.saveOrUpdate(weFission)){

            //裂变消息入库
            List<WeFissionNotice> weFissionNotices =new ArrayList<>();

            WeAddGroupMessageQuery messageQuery = new WeAddGroupMessageQuery();
            messageQuery.setIsAll(false);
            messageQuery.setMsgSource(4);
            messageQuery.setLoginUser(SecurityUtils.getLoginUser());
            messageQuery.setContent(weFission.getContent());
            messageQuery.setBusinessIds(weFission.getId().toString());
            //构建发送素材
            messageQuery.setAttachmentsList(
                    ListUtil.toList(WeMessageTemplate.builder()
                            .msgType(MediaType.LINK.getMediaType())
                            .linkUrl(weFission.getFissionUrl())
                            .build())
            );

            if(DateUtils.parseDate(weFission.getBeginTime())//定时发送,活动时间
                    .after(new Date())){
                messageQuery.setSendTime(DateUtils.parseDate(weFission.getBeginTime()));
                messageQuery.setIsTask(1);
            }else{ //立即发送
                messageQuery.setIsTask(0);
            }

            //构建群发时间
            List<WeAddGroupMessageQuery.SenderInfo> senderInfos = new ArrayList<>();

            //任务宝
            if(TaskFissionType.USER_FISSION.getCode()
                    .equals(weFission.getFassionType())){
                messageQuery.setChatType(1);
                List<WeCustomersVo> weCustomersVos = iWeCustomerService.findWeCustomersForCommonAssembly(
                        weFission.getExecuteUserOrGroup()
                );

                if(CollectionUtil.isNotEmpty(weCustomersVos)){
                   weCustomersVos.stream()
                            .collect(Collectors.groupingBy(WeCustomersVo::getFirstUserId))
                           .forEach((k,v)->{
                               senderInfos.add(
                                       WeAddGroupMessageQuery
                                               .SenderInfo
                                               .builder()
                                               .userId(k)
                                               .customerList(v.stream().map(WeCustomersVo::getExternalUserid).collect(Collectors.toList()))
                                               .build()
                               );
                               v.stream().forEach(kk->{
                                   weFissionNotices.add(WeFissionNotice.builder()
                                           .fissionId(weFission.getId())
                                           .targetId(kk.getExternalUserid())
                                           .targetType(1)
                                           .sendWeUserid(k)
                                           .build());
                               });

                            });
                }
            //群裂变
            }else if(TaskFissionType.GROUP_FISSION.getCode()
                    .equals(weFission.getFassionType())){
                messageQuery.setChatType(2);
                WeGroupMessageExecuteUsertipVo executeUserOrGroup = weFission.getExecuteUserOrGroup();
                List<WeGroup> weGroups = iWeGroupService.list(new LambdaQueryWrapper<WeGroup>()
                        .in(executeUserOrGroup != null && StringUtils.isNotEmpty(executeUserOrGroup.getWeUserIds()), WeGroup::getOwner,
                                ListUtil.toList(executeUserOrGroup.getWeUserIds().split(","))));
                if(CollectionUtil.isNotEmpty(weGroups)){
                    weGroups.stream().forEach(weGroup -> {

                        weFissionNotices.add(WeFissionNotice.builder()
                                .fissionId(weFission.getId())
                                .targetId(weGroup.getChatId())
                                .targetType(2)
                                .sendWeUserid(weGroup.getOwner())
                                .build());

                    });

                    senderInfos.add(
                            WeAddGroupMessageQuery
                                    .SenderInfo
                                    .builder()
                                    .userId(executeUserOrGroup.getWeUserIds())
                                    .chatList(weGroups.stream().map(WeGroup::getChatId).collect(Collectors.toList()))
                                    .build()
                    );
                }
            }

            //删除以前的
            iWeFissionNoticeService.physicalDelete(weFission.getId());
            iWeFissionNoticeService.saveBatch(weFissionNotices);
            //通知员工群发
            iWeMessagePushService.officialPushMessage(messageQuery);
        }

    }

    @Override
    public List<WeFission> findWeFissions(WeFission weFission) {
        List<WeFission> weFissions = this.baseMapper.findWeFissions(new LambdaQueryWrapper<WeFission>()
                .like(StringUtils.isNotEmpty(weFission.getFassionName()),WeFission::getFassionName,weFission.getFassionName())
                .eq(weFission.getFassionType() != null,WeFission::getFassionType,weFission.getFassionType())
                .eq(weFission.getFassionState() != null,WeFission::getFassionState,weFission.getFassionState()));
        return weFissions;
    }

    @Override
    public WeFissionTabVo findWeFissionTab(Long fissionId) {
        return this.baseMapper.findWeFissionTab(fissionId);
    }

    @Override
    public List<WeFissionTrendVo> findWeFissionTrend(WeFission weFission) {
        return this.baseMapper.findWeFissionTrend(weFission);
    }

    @Override
    public List<WeFissionDataReportVo> findWeFissionDataReport(WeFission weFission) {
        return this.baseMapper.findWeFissionDataReport(weFission);
    }

    @Override
    public List<WeGroupFissionDetailVo> findWeGroupFissionDetail(String customerName, String weUserId,String chatId) {
        return this.baseMapper.findWeGroupFissionDetail(customerName, weUserId,chatId);
    }

    @Override
    public List<WeTaskFissionDetailVo> findWeTaskFissionDetail(String customerName, String weUserId) {
        return this.baseMapper.findWeTaskFissionDetail(customerName, weUserId);
    }

    @Override
    public List<WeFissionDetailSubVo> findWeFissionDetailSub(Long fissionInviterRecordId) {
        return this.baseMapper.findWeFissionDetailSub(fissionInviterRecordId);
    }

    @Override
    public WeFissionInviterPoster findFissionPoster(String unionid, String fissionId) {

        WeFissionInviterPoster weFissionInviterPoster = iWeFissionInviterPosterService.getOne(new LambdaQueryWrapper<WeFissionInviterPoster>()
                .eq(WeFissionInviterPoster::getInviterId, unionid)
                .eq(WeFissionInviterPoster::getFissionId, fissionId));


        WeFissionInviterRecord weFissionInviterRecord = this.builderInviterRecord(unionid, fissionId);

        if(null != weFissionInviterRecord){



            if(null == weFissionInviterPoster){ //为空,重新构建裂变海报
                weFissionInviterPoster=new WeFissionInviterPoster();
                weFissionInviterPoster.setFissionId(Long.parseLong(fissionId));
                weFissionInviterPoster.setInviterId(unionid);
                WeFission weFission = this.getById(fissionId);
                if(null != weFission&&StringUtils.isNotEmpty(weFission.getPosterUrl())){

                    //任务宝
                    if(TaskFissionType.USER_FISSION.getCode()
                            .equals(weFission.getFassionType())){
                        String weUserIds=null;String deptIds=null;String positions=null;

                        WeSopExecuteUserConditVo addWeUser = weFission.getAddWeUserOrGroupCode().getAddWeUser();

                        if(null != addWeUser){
                            WeSopExecuteUserConditVo.ExecuteUserCondit executeUserCondit = addWeUser.getExecuteUserCondit();
                            if(null != executeUserCondit){
                                List<String> weUserIdss = executeUserCondit.getWeUserIds();
                                if(CollectionUtil.isNotEmpty(weUserIdss)){
                                    weUserIds=StringUtils.join(weUserIdss,",");
                                }
                            }

                            WeSopExecuteUserConditVo.ExecuteDeptCondit executeDeptCondit = addWeUser.getExecuteDeptCondit();
                            if(null != executeDeptCondit){
                                List<String> deptIdss = executeDeptCondit.getDeptIds();
                                if(CollectionUtil.isNotEmpty(deptIdss)){
                                    deptIds=StringUtils.join(deptIdss,",");
                                }
                                List<String> posts = executeDeptCondit.getPosts();
                                if(CollectionUtil.isNotEmpty(posts)){
                                    positions=StringUtils.join(posts,",");
                                }
                            }
                        }


                        AjaxResult<List<String>> listAjaxResult
                                = qwSysUserClient.screenConditWeUser(weUserIds,deptIds,positions);
                        if(null != listAjaxResult){
                            List<String> addWeUserIds = listAjaxResult.getData();

                            if(CollectionUtil.isEmpty(addWeUserIds)){
                                throw new WeComException("当前添加的员工不存在或为无效员工");
                            }
                            WeAddWayVo weAddWayVo = iWeQrCodeService.createQrbyWeUserIds(
                                    addWeUserIds,
                                    WeConstans.FISSION_PREFIX_RWB + weFissionInviterRecord.getId()
                            );

                            if(weAddWayVo.getErrCode() !=null && WeConstans.WE_SUCCESS_CODE.equals(weAddWayVo.getErrCode())) {

                                weFissionInviterPoster.setState(WeConstans.FISSION_PREFIX_RWB + weFissionInviterRecord.getId());
                                if(weAddWayVo != null){

                                    WeMaterial material = materialService.builderPosterWeMaterial( weAddWayVo.getQrCode(),weFission.getPosterId());
                                    if(null != material){
                                        weFissionInviterPoster.setFissionPosterUrl(material.getMaterialUrl());
                                    }
                                    weFissionInviterPoster.setConfig(weAddWayVo.getConfigId());
                                }

                            }else{
                                throw new WeComException(weAddWayVo.getErrMsg());
                            }

                        }


                        //群裂变
                    }else if(TaskFissionType.GROUP_FISSION.getCode()
                            .equals(weFission.getFassionType())){

                        WeGroupCode weGroupCode = weFission.getAddWeUserOrGroupCode().getAddGroupCode();

                        weGroupCode.setState(WeConstans.FISSION_PREFIX_QLB + weFissionInviterRecord.getId());


                        WeGroupChatAddJoinWayVo addJoinWayVo = iWeGroupCodeService.builderGroupCodeUrl(weGroupCode);

                        if(null != addJoinWayVo && StringUtils.isNotEmpty(addJoinWayVo.getConfig_id())){

                            weFissionInviterPoster.setState(weGroupCode.getState());
                            weFissionInviterPoster.setConfig(weGroupCode.getConfigId());

                            WeMaterial material = materialService.builderPosterWeMaterial( weGroupCode.getCodeUrl(),weFission.getPosterId());
                            if(null != material){
                                weFissionInviterPoster.setFissionPosterUrl(material.getMaterialUrl());
                            }

                        }

                    }

                }


                //入库
                iWeFissionInviterPosterService.save(weFissionInviterPoster);
            }


        }

        return weFissionInviterPoster;

    }

    @Override
    @Async
    public void handleTaskFissionRecord(String state, WeCustomer weCustomer) {

        if(StringUtils.isNotEmpty(state) && state.startsWith(WeConstans.FISSION_PREFIX_RWB)){
            String fissionInviterRecordId = state.substring(WeConstans.FISSION_PREFIX_RWB.length());
            this.handleFissionRecord(fissionInviterRecordId,
                    WeFissionInviterRecordSub.builder()
                            .addTargetId(weCustomer.getAddUserId())
                            .fissionInviterRecordId(Long.parseLong(fissionInviterRecordId))
                            .addTargetType(1)
                            .inviterUserName(weCustomer.getCustomerName())
                            .build()
            );
        }




    }

    @Override
    @Async
    public void handleGroupFissionRecord(String state, WeGroupMember weGroupMember) {

        if(StringUtils.isNotEmpty(state) && state.startsWith(WeConstans.FISSION_PREFIX_QLB)){
            String fissionInviterRecordId = state.substring(WeConstans.FISSION_PREFIX_QLB.length());


            this.handleFissionRecord(fissionInviterRecordId,
                    WeFissionInviterRecordSub.builder()
                            .addTargetId(weGroupMember.getChatId())
                            .fissionInviterRecordId(Long.parseLong(fissionInviterRecordId))
                            .addTargetType(2)
                            .inviterUserName(weGroupMember.getName())
                            .build()
            );
        }




    }



    private void handleFissionRecord(String fissionInviterRecordId,WeFissionInviterRecordSub weFissionInviterRecordSub){

        WeFissionInviterRecord weFissionInviterRecord
                = iWeFissionInviterRecordService.getById(fissionInviterRecordId);
        if(null != weFissionInviterRecord){
            iWeFissionInviterRecordSubService.save(weFissionInviterRecordSub);

            int inviterRecordNumber = iWeFissionInviterRecordSubService.count(new LambdaQueryWrapper<WeFissionInviterRecordSub>()
                    .eq(WeFissionInviterRecordSub::getFissionInviterRecordId, fissionInviterRecordId));

            //设置邀请员工数
            weFissionInviterRecord.setInviterNumber(inviterRecordNumber);

            WeFission weFission = this.getById(weFissionInviterRecord.getFissionId());

            if(null != weFission){
                //当前状态设置为已完成
                if(weFission.getExchangeTip()<=inviterRecordNumber){
                    weFissionInviterRecord.setInviterState(1);
                }
            }

            iWeFissionInviterRecordService.updateById(weFissionInviterRecord);



        }

    }

    //生成邀请记录
    private WeFissionInviterRecord builderInviterRecord(String unionid, String fissionId){
        WeFissionInviterRecord weFissionInviterRecord = iWeFissionInviterRecordService.getOne(new LambdaQueryWrapper<WeFissionInviterRecord>()
                .eq(WeFissionInviterRecord::getInviterUnionid, unionid)
                .eq(WeFissionInviterRecord::getFissionId, fissionId));


        if(null == weFissionInviterRecord){

            WeFission weFission = this.getById(fissionId);
            if(null != weFission){
                weFissionInviterRecord = WeFissionInviterRecord.builder()
                        .fissionId(Long.parseLong(fissionId))
                        .inviterUnionid(unionid)
                        .inviterNumber(weFission.getExchangeTip())
                        .build();
                //入库一份邀请记录
                iWeFissionInviterRecordService.save(
                        weFissionInviterRecord
                );

            }

        }


        return weFissionInviterRecord;

    }
}




