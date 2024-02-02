package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.enums.TaskFissionType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.fission.*;
import com.linkwechat.domain.fission.vo.*;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.groupmsg.vo.WeGroupMessageExecuteUsertipVo;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.moments.entity.WeMomentsTask;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatGetJoinWayVo;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.*;
import com.linkwechat.mapper.WeFissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    @Lazy
    private IWeCustomerService iWeCustomerService;


    @Autowired
    private IWeMessagePushService iWeMessagePushService;


    @Autowired
    @Lazy
    private IWeGroupCodeService iWeGroupCodeService;

    @Autowired
    @Lazy
    private IWeGroupService iWeGroupService;

    @Autowired
    private QwSysUserClient qwSysUserClient;


    @Autowired
    private IWeQrCodeService iWeQrCodeService;


    @Autowired
    private IWeMaterialService materialService;


    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @Autowired
    private IWeGroupMemberService iWeGroupMemberService;

    @Override
    @Transactional
    public void buildWeFission(WeFission weFission) {

        if(null == weFission.getAddWeUserOrGroupCode()){
            throw new WeComException("当前添加的成员或客群不可为空");
        }

        if(weFission.getId()==null){
            weFission.setId(SnowFlakeUtil.nextId());
            //裂变h5链接
            weFission.setFissionUrl(
                    MessageFormat.format(linkWeChatConfig.getFissionUrl(), weFission.getId().toString())
            );
        }


        if(weFission.getActiveCoverType()==null || weFission.getActiveCoverType().equals(1)){ //当前封面为海报
            weFission.setActiveCoverUrl(weFission.getPosterUrl());
        }




        WeFission.ExchangeContent exchangeContent = weFission.getExchangeContent();

        if(null != exchangeContent){

            if(StringUtils.isNotEmpty(exchangeContent.getWeUserId())){
                WeAddWayVo weAddWayVo = iWeQrCodeService.createQrbyWeUserIds(
                        ListUtil.toList(exchangeContent.getWeUserId()),
                        WeConstans.FISSION_CUSTOMER_SERVICE + weFission.getId()
                );

                if(weAddWayVo.getErrCode() !=null && WeConstans.WE_SUCCESS_CODE.equals(weAddWayVo.getErrCode())) {
                    exchangeContent.setCustomerServiceCodeUrl(
                            weAddWayVo.getQrCode()
                    );
                    weFission.setExchangeContent(
                            exchangeContent
                    );
                }

            }


        }


        if(this.saveOrUpdate(weFission)){

            //裂变消息入库
            List<WeFissionNotice> weFissionNotices =new ArrayList<>();

            //任务宝
            if(TaskFissionType.USER_FISSION.getCode()
                    .equals(weFission.getFassionType())){

                List<WeCustomersVo> weCustomersVos=new ArrayList<>();
                if(new Integer(0).equals(weFission.getScopeType())){ //全部
                    List<WeCustomersVo> limitWeCustomerList = iWeCustomerService.findLimitWeCustomerList();
                    if(CollectionUtil.isNotEmpty(limitWeCustomerList)){
                        weCustomersVos.addAll(limitWeCustomerList);
                    }
                }else if(new Integer(1).equals(weFission.getScopeType())){//部分
                    List<WeAddGroupMessageQuery.SenderInfo> senderList
                            = weFission.getSenderList();
                    if(CollectionUtil.isNotEmpty(senderList)){
                        senderList.stream().forEach(k->{
                            List<WeCustomersVo> weCustomerList = iWeCustomerService.findWeCustomerList(WeCustomersQuery.builder()
                                    .firstUserId(k.getUserId())
                                    .externalUserids(StringUtils.join(k.getCustomerList(), ","))
                                    .build(), null);
                            if(CollectionUtil.isNotEmpty(weCustomerList)){
                                weCustomersVos.addAll(weCustomerList);
                            }
                        });
                    }
                }

                if(CollectionUtil.isNotEmpty(weCustomersVos)){
                   weCustomersVos.stream()
                            .collect(Collectors.groupingBy(WeCustomersVo::getFirstUserId))
                           .forEach((k,v)->{
                               v.stream().forEach(kk->{
                                   weFissionNotices.add(WeFissionNotice.builder()
                                           .fissionId(weFission.getId())
                                           .targetId(kk.getExternalUserid())
                                           .targetSubId(kk.getUnionid())
                                           .targetType(1)
                                           .sendWeUserid(k)
                                           .build());
                               });

                            });
                }


            //群裂变
            }else if(TaskFissionType.GROUP_FISSION.getCode()
                    .equals(weFission.getFassionType())){
                WeGroupMessageExecuteUsertipVo executeUserOrGroup = weFission.getExecuteUserOrGroup();

                LambdaQueryWrapper<WeGroup> weGroupLambdaQueryWrapper = new LambdaQueryWrapper<>();
                if(executeUserOrGroup != null && StringUtils.isNotEmpty(executeUserOrGroup.getWeUserIds())){
                    weGroupLambdaQueryWrapper.in(
                            WeGroup::getOwner,
                            ListUtil.toList(executeUserOrGroup.getWeUserIds().split(","))
                    );
                }
                List<WeGroup> weGroups = iWeGroupService.list(weGroupLambdaQueryWrapper);
                if(CollectionUtil.isNotEmpty(weGroups)){

                    List<WeGroupMember> weGroupMembers = iWeGroupMemberService.list(new LambdaQueryWrapper<WeGroupMember>()
                                    .eq(WeGroupMember::getType,2)
                            .in(WeGroupMember::getChatId, weGroups.stream().map(WeGroup::getChatId).collect(Collectors.toList())));
                    if(CollectionUtil.isNotEmpty(weGroupMembers)){

                        weGroupMembers.stream().forEach(weGroupMember -> {
                            weFissionNotices.add(WeFissionNotice.builder()
                                    .fissionId(weFission.getId())
                                    .targetId(weGroupMember.getChatId())
                                    .targetType(2)
                                     .targetSubId(weGroupMember.getUnionId())
                                    .sendWeUserid(
                                            weGroups.stream().filter(weGroup -> weGroup.getChatId().equals(weGroupMember.getChatId())).findAny().get().getOwner()
                                    )
                                    .build());

                        });

                    }


                }
            }

            //删除以前的
            iWeFissionNoticeService.physicalDelete(weFission.getId());
            iWeFissionNoticeService.saveBatch(weFissionNotices);

        }

    }

    @Override
    public List<WeFission> findWeFissions(WeFission weFission) {
        List<WeFission> weFissions = this.baseMapper.findWeFissions(new LambdaQueryWrapper<WeFission>()
                .like(StringUtils.isNotEmpty(weFission.getFassionName()),WeFission::getFassionName,weFission.getFassionName())
                .eq(weFission.getFassionType() != null,WeFission::getFassionType,weFission.getFassionType())
                .eq(weFission.getFassionState() != null,WeFission::getFassionState,weFission.getFassionState())
                        .eq(WeFission::getDelFlag, Constants.NORMAL_CODE)
                .orderByDesc(WeFission::getUpdateTime));
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
    public List<WeGroupFissionDetailVo> findWeGroupFissionDetail(String fissionId,String customerName, String weUserId,String chatId) {
        return this.baseMapper.findWeGroupFissionDetail(fissionId,customerName, weUserId,chatId);
    }

    @Override
    public List<WeTaskFissionDetailVo> findWeTaskFissionDetail(String fissionId,String customerName, String weUserId) {
        return this.baseMapper.findWeTaskFissionDetail(fissionId,customerName, weUserId);
    }

    @Override
    public List<WeFissionDetailSubVo> findWeFissionDetailSub(Long fissionInviterRecordId) {
        return this.baseMapper.findWeFissionDetailSub(fissionInviterRecordId);
    }

    @Override
    public WeFissionInviterPoster findFissionPoster(String unionid, String fissionId) throws Exception {

        WeFissionInviterPoster weFissionInviterPoster=null;

        List<WeFissionInviterPoster> weFissionInviterPosters = iWeFissionInviterPosterService.list(new LambdaQueryWrapper<WeFissionInviterPoster>()
                .eq(WeFissionInviterPoster::getInviterId, unionid)
                .eq(WeFissionInviterPoster::getFissionId, fissionId));


        if(CollectionUtil.isEmpty(weFissionInviterPosters)){

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


                            WeGroupChatGetJoinWayVo addJoinWayVo = iWeGroupCodeService.builderGroupCodeUrl(weGroupCode);

                            if(null != addJoinWayVo){
                                WeGroupChatGetJoinWayVo.JoinWay joinWay = addJoinWayVo.getJoin_way();
                                if(joinWay != null){

                                    weFissionInviterPoster.setState(weGroupCode.getState());
                                    weFissionInviterPoster.setConfig(joinWay.getConfig_id());

                                    WeMaterial material = materialService.builderPosterWeMaterial( joinWay.getQr_code(),weFission.getPosterId());
                                    if(null != material){
                                        weFissionInviterPoster.setFissionPosterUrl(material.getMaterialUrl());
                                    }
                                }


                            }

                        }

                    }


                    //入库
                    iWeFissionInviterPosterService.save(weFissionInviterPoster);
                }


            }


        }else{
            weFissionInviterPoster =weFissionInviterPosters.stream().findFirst().get();


        }

        return weFissionInviterPoster;

    }

    @Override
    @Async
    public void handleTaskFissionRecord(String state, WeCustomer weCustomer) {

        if(StringUtils.isNotEmpty(state) && state.startsWith(WeConstans.FISSION_PREFIX_RWB)){
            String fissionInviterRecordId = state.substring(WeConstans.FISSION_PREFIX_RWB.length());

            WeFissionInviterRecordSub weFissionInviterRecordSub = WeFissionInviterRecordSub.builder()
                    .addTargetId(weCustomer.getAddUserId())
                    .fissionInviterRecordId(Long.parseLong(fissionInviterRecordId))
                    .userId(weCustomer.getExternalUserid())
                    .addTargetType(1)
                    .avatar(weCustomer.getAvatar())
                    .inviterUserName(weCustomer.getCustomerName())
                    .build();
            this.handleFissionRecord(fissionInviterRecordId,
                    weFissionInviterRecordSub
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
                            .userId(weGroupMember.getUserId())
                            .fissionInviterRecordId(Long.parseLong(fissionInviterRecordId))
                            .addTargetType(2)
                            .inviterUserName(weGroupMember.getName())
                            .build()
            );
        }
    }


    /**
     * 处理裂变任务
     */
    @Override
    public void handleFission() {


        //查询处未期的裂变任务
        List<WeFission> weFissions = this.list(new LambdaQueryWrapper<WeFission>()
                .isNotNull(WeFission::getAddWeUserOrGroupCode)
                        .eq(WeFission::getIsTip,2)
                .notIn(WeFission::getFassionState, ListUtil.toList(3,4)));

        try {


            if(CollectionUtil.isNotEmpty(weFissions)){


                weFissions.stream().forEach(weFission -> {


                    //如果当前时间在裂变结束时间之前,则裂变结束
                    if(weFission.getFassionEndTime().getTime()<System.currentTimeMillis()){
                        weFission.setFassionState(3);
                    }

                    //如果当前时间是裂变开始时间与结束时间之间,则裂变开始
                    if(new Date().after(weFission.getFassionStartTime())&&
                            new Date().before(weFission.getFassionEndTime())
                    ){

                        //发送通知逻辑
                        if(weFission.getIsTip().equals(new Integer(2))){
                            weFission.setIsTip(1);
                            WeAddGroupMessageQuery messageQuery = new WeAddGroupMessageQuery();
                            messageQuery.setIsAll(false);
                            messageQuery.setMsgSource(4);
                            messageQuery.setIsTask(0);
                            messageQuery.setLoginUser(SecurityUtils.getLoginUser());

                            messageQuery.setBusinessIds(weFission.getId().toString());
                            messageQuery.setSendTime(null);

                            List<WeMessageTemplate> attachmentsList=new ArrayList<>();



                            //发送文本
                            if(StringUtils.isNotEmpty(weFission.getContent())){
                                messageQuery.setContent(weFission.getContent());
                                attachmentsList.add(
                                        WeMessageTemplate.builder()
                                                .msgType(MediaType.TEXT.getMediaType())
                                                .content(weFission.getContent())
                                                .build()
                                );
                            }


                            //发送链接
                            WeMaterial weMaterial = materialService.getById(weFission.getPosterId());
                            if(null != weMaterial){
                                attachmentsList.add(
                                        WeMessageTemplate.builder()
                                                .title(weFission.getActiveTitle())
                                                .description(weFission.getActiveDescr())
                                                .picUrl(weFission.getActiveCoverUrl())
                                                .msgType(MediaType.LINK.getMediaType())
                                                .linkUrl(weFission.getFissionUrl())
                                                .build()
                                );

                            }

                            messageQuery.setAttachmentsList(
                                    attachmentsList
                            );


                            //构建群发时间
                            List<WeAddGroupMessageQuery.SenderInfo> senderInfos = new ArrayList<>();

                            //任务宝
                            if(TaskFissionType.USER_FISSION.getCode()
                                    .equals(weFission.getFassionType())){
                                messageQuery.setChatType(1);

                                List<WeFissionNotice> weFissionNotices = iWeFissionNoticeService.list(new LambdaQueryWrapper<WeFissionNotice>()
                                        .eq(WeFissionNotice::getFissionId, weFission.getId()));

                                if(CollectionUtil.isNotEmpty(weFissionNotices)){
                                    weFissionNotices.stream()
                                            .collect(Collectors.groupingBy(WeFissionNotice::getSendWeUserid))
                                            .forEach((k,v)->{
                                                senderInfos.add(
                                                        WeAddGroupMessageQuery
                                                                .SenderInfo
                                                                .builder()
                                                                .userId(k)
                                                                .customerList(v.stream().map(WeFissionNotice::getTargetId).collect(Collectors.toList()))
                                                                .build()
                                                );

                                            });
                                }

                                messageQuery.setSenderList(senderInfos);


                                //通知员工群发
                                iWeMessagePushService.officialPushMessage(messageQuery);

                                //群裂变
                            } else if(TaskFissionType.GROUP_FISSION.getCode()
                                    .equals(weFission.getFassionType())){
                                messageQuery.setChatType(2);
                                WeGroupMessageExecuteUsertipVo executeUserOrGroup = weFission.getExecuteUserOrGroup();
                                List<WeGroup> weGroups =new ArrayList<>();
                                if(executeUserOrGroup != null && StringUtils.isNotEmpty(executeUserOrGroup.getWeUserIds())){
                                    weGroups = iWeGroupService.list(new LambdaQueryWrapper<WeGroup>()
                                            .in(executeUserOrGroup != null && StringUtils.isNotEmpty(executeUserOrGroup.getWeUserIds()), WeGroup::getOwner,
                                                    ListUtil.toList(executeUserOrGroup.getWeUserIds().split(","))));
                                }else{
                                    weGroups=iWeGroupService.list();
                                }


                                if(CollectionUtil.isNotEmpty(weGroups)){
                                    Map<String, List<WeGroup>> weGroupMap
                                            = weGroups.stream().collect(Collectors.groupingBy(WeGroup::getOwner));

                                    weGroupMap.forEach((k,v)->{

                                        senderInfos.add(
                                                WeAddGroupMessageQuery
                                                        .SenderInfo
                                                        .builder()
                                                        .userId(k)
                                                        .chatList(v.stream().map(WeGroup::getChatId).collect(Collectors.toList()))
                                                        .build()
                                        );

                                        messageQuery.setSenderList(senderInfos);



                                    });

                                    //通知员工群发
                                    iWeMessagePushService.officialPushMessage(messageQuery);

                                }


                            }


                        }


                        weFission.setFassionState(2);
                    }



                });




            }

        }catch (Exception e){

            if(CollectionUtil.isNotEmpty(weFissions)){
                weFissions.stream().forEach(weFission ->{
                    weFission.setIsTip(3);
                });
            }


        }finally {
            if(CollectionUtil.isNotEmpty(weFissions)){
                this.updateBatchById(weFissions);
            }


        }




    }

    @Override
    public void handleExpireFission() {
        this.update(WeFission.builder().fassionState(3).build(),new LambdaQueryWrapper<WeFission>()
                .apply("date_format (fassion_end_time,'%Y-%m-%d %H:%i') <= date_format ({0},'%Y-%m-%d %H:%i')",new Date()));
    }

    @Override
    public void updateBatchFissionIsTipNoSend(List<WeFission> weFissions) {
        this.baseMapper.updateBatchFissionIsTipNoSend(weFissions);
    }

    @Override
    public void getXX() {
        String currentDir = System.getProperty("user.dir");
        String libPath = currentDir + "/resources/libWeWorkFinanceSdk_Java.so";
        System.load(libPath);
    }

    private void handleFissionRecord(String fissionInviterRecordId,WeFissionInviterRecordSub weFissionInviterRecordSub){

        WeFissionInviterRecord weFissionInviterRecord
                = iWeFissionInviterRecordService.getById(fissionInviterRecordId);
        if(null != weFissionInviterRecord){
            weFissionInviterRecordSub.setId(SnowFlakeUtil.nextId());
            weFissionInviterRecordSub.setCreateBy(SecurityUtils.getUserName());
            weFissionInviterRecordSub.setCreateTime(new Date());
            weFissionInviterRecordSub.setCreateById(SecurityUtils.getUserId());
            weFissionInviterRecordSub.setUpdateBy(SecurityUtils.getUserName());
            weFissionInviterRecordSub.setUpdateTime(new Date());
            weFissionInviterRecordSub.setUpdateById(SecurityUtils.getUserId());
            weFissionInviterRecordSub.setDelFlag(Constants.COMMON_STATE);
            iWeFissionInviterRecordSubService.batchSaveOrUpdate(ListUtil.toList(weFissionInviterRecordSub));

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
//                        .inviterNumber(weFission.getExchangeTip())
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




