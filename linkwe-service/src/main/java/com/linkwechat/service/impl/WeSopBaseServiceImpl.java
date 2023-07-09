package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Joiner;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.enums.SopExecuteStatus;
import com.linkwechat.common.enums.SopType;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.WeekDateUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.groupchat.query.WeGroupChatQuery;
import com.linkwechat.domain.groupchat.vo.LinkGroupChatListVo;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.sop.*;
import com.linkwechat.domain.sop.dto.WeSopBaseDto;
import com.linkwechat.domain.sop.dto.WeSopPushTimeDto;
import com.linkwechat.domain.sop.vo.*;
import com.linkwechat.domain.sop.vo.content.*;
import com.linkwechat.domain.wecom.query.customer.msg.WeGetGroupMsgListQuery;
import com.linkwechat.domain.wecom.vo.customer.msg.WeGroupMsgListVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.fegin.QwDeptClient;
import com.linkwechat.fegin.QwSysDeptClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeSopBaseMapper;
import com.linkwechat.mapper.WeSopPushTimeMapper;
import com.linkwechat.service.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author robin
 * @description 针对表【we_sop_base(Sop base表)】的数据库操作Service实现
 * @createDate 2022-09-06 10:33:34
 */
@Service
public class WeSopBaseServiceImpl extends ServiceImpl<WeSopBaseMapper, WeSopBase>
        implements IWeSopBaseService {

    @Autowired
    private IWeSopAttachmentsService iWeSopAttachmentsService;


    @Autowired
    private IWeSopPushTimeService iWeSopPushTimeService;



    @Lazy
    @Autowired
    private IWeSopExecuteTargetAttachmentsService iWeSopExecuteTargetAttachmentsService;


    @Autowired
    private IWeSopExecuteTargetService executeTargetService;


    @Autowired
    private IWeGroupService iWeGroupService;


    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;



    @Autowired
    private QwCustomerClient qwCustomerClient;


    @Autowired
    private IWeCustomerService iWeCustomerService;


    @Autowired
    private QwSysUserClient qwSysUserClient;



    @Autowired
    private QwSysDeptClient qwSysDeptClient;







    @Override
    @Transactional
    public void createWeSop(WeSopBase weSopBase) {
        if(save(weSopBase)){
            List<WeSopPushTime> weSopPushTimes = weSopBase.getWeSopPushTimes();
            if(CollectionUtil.isNotEmpty(weSopPushTimes)){
                weSopPushTimes.stream().forEach(weSopPushTime -> {
                    weSopPushTime.setSopBaseId(weSopBase.getId());
                    if(iWeSopPushTimeService.save(weSopPushTime)){
                        List<WeMessageTemplate> weMessageTemplates = weSopPushTime.getAttachments();
                        if(CollectionUtil.isNotEmpty(weMessageTemplates)){
                            iWeSopAttachmentsService.saveBatchBySopBaseId(weSopPushTime.getId(),weSopBase.getId(),weMessageTemplates);
                            //发送mq消息生成执行任务
                            rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getSopEx(), rabbitMQSettingConfig.getSopRk(), JSONObject.toJSONString(
                                    WeSopBaseDto.builder()
                                            .sopBaseId(weSopBase.getId())
                                            .isCreateOrUpdate(true).loginUser(
                                                    SecurityUtils.getLoginUser()
                                            ).build()
                            ));

                        }
                    }
                });
            }
        }
    }


    @Override
    @Transactional
    public void updateWeSop(WeSopBase weSopBase) {
        if(updateById(weSopBase)){
            List<WeSopPushTime> weSopPushTimes = weSopBase.getWeSopPushTimes();

            if(CollectionUtil.isEmpty(weSopPushTimes)){ //任何值未传,则证明需要删除所有内容相关
                if(iWeSopPushTimeService.remove(new LambdaQueryWrapper<WeSopPushTime>()
                        .eq(WeSopPushTime::getSopBaseId,weSopBase.getId()))){
                    iWeSopAttachmentsService.remove(new LambdaQueryWrapper<WeSopAttachments>()
                            .eq(WeSopAttachments::getSopBaseId,weSopBase.getId()));
                }
            }else{
                //删除当前编辑下移除的任务时间以及素材
                List<Long> noRemoveIds
                        = weSopPushTimes.stream().filter(e -> e.getId() != null).map(WeSopPushTime::getId).collect(Collectors.toList());
                if(CollectionUtil.isNotEmpty(noRemoveIds)){
                    if(iWeSopPushTimeService.remove(new LambdaQueryWrapper<WeSopPushTime>()
                            .eq(WeSopPushTime::getSopBaseId,weSopBase.getId())
                            .notIn(WeSopPushTime::getId,noRemoveIds))){
                        iWeSopAttachmentsService.remove(new LambdaQueryWrapper<WeSopAttachments>()
                                .eq(WeSopAttachments::getSopBaseId,weSopBase.getId())
                                .notIn(WeSopAttachments::getSopPushTimeId,noRemoveIds));
                    }
                }

                weSopPushTimes.stream().forEach(weSopPushTime -> {
                    //新增或者编辑相关的
                    weSopPushTime.setSopBaseId(weSopBase.getId());
                    if(iWeSopPushTimeService.saveOrUpdate(weSopPushTime)){
                        List<WeMessageTemplate> weMessageTemplates = weSopPushTime.getAttachments();
                        if(CollectionUtil.isNotEmpty(weMessageTemplates)){
                            iWeSopAttachmentsService.updateBatchBySopBaseId(weSopPushTime.getId(),weSopBase.getId(),weMessageTemplates);
                        }
                    }

                });




            }


            //发送mq消息生成执行任务
            rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getSopEx(), rabbitMQSettingConfig.getSopRk(), JSONObject.toJSONString(
                    WeSopBaseDto.builder()
                            .sopBaseId(weSopBase.getId())
                            .isCreateOrUpdate(false).loginUser(
                                    SecurityUtils.getLoginUser()
                            ).build()
            ));


        }

    }

    @Override
    public WeSopBase findWeSopBaseBySopBaseId(Long sopBaseId) {
        WeSopBase weSopBase
                = getById(sopBaseId);
        if(null != weSopBase){
            List<WeSopPushTime> sopPushTimes = iWeSopPushTimeService.list(new LambdaQueryWrapper<WeSopPushTime>()
                    .eq(WeSopPushTime::getSopBaseId, sopBaseId));

            if(CollectionUtil.isNotEmpty(sopPushTimes)){
                sopPushTimes.stream().forEach(sopPushTime->{
                    List<WeSopAttachments> weSopAttachments = iWeSopAttachmentsService.list(new LambdaQueryWrapper<WeSopAttachments>()
                            .eq(WeSopAttachments::getSopPushTimeId, sopPushTime.getId()));

                    sopPushTime.setWeSopAttachments(
                            weSopAttachments
                    );
                    sopPushTime.setAttachments(
                            iWeSopAttachmentsService.weSopAttachmentsToTemplate(weSopAttachments)
                    );
                });
            }
            weSopBase.setWeSopPushTimes(sopPushTimes);
        }


        return weSopBase;
    }


    @Override
    @Transactional
    public void removeWeSoPBySopId(List<Long> sopId) {

        removeByIds(sopId);
        iWeSopPushTimeService.remove(new LambdaQueryWrapper<WeSopPushTime>()
                .in(WeSopPushTime::getSopBaseId,sopId));
        iWeSopAttachmentsService.remove(new LambdaQueryWrapper<WeSopAttachments>()
                .in(WeSopAttachments::getSopBaseId,sopId));

    }

    @Override
    public TableDataInfo<List<WeSopListsVo>> findWeSopListsVo(WeSopBase weSopBase) {

        TableDataInfo<List<WeSopListsVo>> tableDataInfo=new TableDataInfo();
        tableDataInfo.setCode(HttpStatus.SUCCESS);

        List<WeSopListsVo> weSopListsVos=new ArrayList<>();

        PageHelper.startPage(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize());
        List<WeSopBase> weSopBases = this.list(new LambdaQueryWrapper<WeSopBase>()
                .like(StringUtils.isNotEmpty(weSopBase.getSopName()),WeSopBase::getSopName, weSopBase.getSopName())
                .eq(weSopBase.getBaseType() !=null,WeSopBase::getBaseType, weSopBase.getBaseType())
                .eq(weSopBase.getSopState() !=null,WeSopBase::getSopState, weSopBase.getSopState())
                .eq(weSopBase.getBusinessType() !=null,WeSopBase::getBusinessType, weSopBase.getBusinessType())
                .apply(StringUtils.isNotEmpty(weSopBase.getBeginTime())&&StringUtils.isNotEmpty(weSopBase.getEndTime()),
                        "date_format(create_time,'%Y-%m-%d') BETWEEN '"+
                                weSopBase.getBeginTime()
                                +"' AND '"+
                                weSopBase.getEndTime()+"'")
                .orderByDesc(WeSopBase::getCreateTime)
        );

        PageInfo<WeSopBase> pageInfo = new PageInfo<>(weSopBases);

        if(CollectionUtil.isNotEmpty(pageInfo.getList())){
            pageInfo.getList().stream().forEach(k->{
                WeSopListsVo weSopListsVo = WeSopListsVo.builder()
                        .businessType(k.getBusinessType())
                        .sopBaseId(k.getId())
                        .sopName(k.getSopName())
                        .createBy(k.getCreateBy())
                        .createTime(k.getCreateTime())
                        .sopState(k.getSopState())
                        .sendType(k.getSendType())
                        .build();
                //设置执行成员
                WeSopExecuteUserConditVo executeWeUser = k.getExecuteWeUser();
                if(Objects.isNull(executeWeUser)){//执行成员为空则查询当前系统所有员工

                    AjaxResult<List<SysUser>> listAjaxResult = qwSysUserClient.findAllSysUser(null,null,null);

                    if(null != listAjaxResult&&CollectionUtil.isNotEmpty(listAjaxResult.getData())){
                        weSopListsVo.setExecuteUser(
                                listAjaxResult.getData().stream().map(SysUser::getUserName).collect(Collectors.joining(","))
                        );
                    }
                }else{//查询成员，或者部门岗位
                    StringBuilder sb=new StringBuilder();
                    //设置具体执行员工
                    WeSopExecuteUserConditVo.ExecuteUserCondit executeUserCondit
                            = executeWeUser.getExecuteUserCondit();
                    if(null != executeUserCondit && executeUserCondit.isChange()
                            && CollectionUtil.isNotEmpty(executeUserCondit.getWeUserIds())){

                        AjaxResult<List<SysUser>> listAjaxResult = qwSysUserClient.findAllSysUser(
                                Joiner.on(",").join(executeUserCondit.getWeUserIds()),null,null);

                        if(null != listAjaxResult&&CollectionUtil.isNotEmpty(listAjaxResult.getData())){

                            sb.append(
                                    listAjaxResult.getData().stream().map(SysUser::getUserName).collect(Collectors.joining(","))
                            );
                        }


                    }

                    //设置执行的部门或岗位
                    WeSopExecuteUserConditVo.ExecuteDeptCondit executeDeptCondit
                            = executeWeUser.getExecuteDeptCondit();
                    if(null != executeDeptCondit && executeDeptCondit.isChange()){
                        if(CollectionUtil.isNotEmpty(executeDeptCondit.getDeptIds())){//设置部门
                            AjaxResult<List<SysDept>> result
                                    = qwSysDeptClient.findSysDeptByIds(StringUtils.join(executeDeptCondit.getDeptIds(), ","));

                            if(null != result && CollectionUtil.isNotEmpty(result.getData())){
                                sb.append(",").append(result.getData().stream().map(SysDept::getDeptName).collect(Collectors.joining(",")));
                            }


                        }

                        if(CollectionUtil.isNotEmpty(executeDeptCondit.getPosts())){//设置岗位
                            sb.append(",").append( Joiner.on(",").join(executeDeptCondit.getPosts()));

                        }

                    }

                    weSopListsVo.setExecuteUser(sb.toString());
                }


                //设置sop数据
                List<WeSopPushTime> weSopPushTimes = iWeSopPushTimeService.list(new LambdaQueryWrapper<WeSopPushTime>()
                        .eq(WeSopPushTime::getSopBaseId, k.getId()));
                if(CollectionUtil.isNotEmpty(weSopPushTimes)){
                    //推送所需要的次数
                    weSopListsVo.setPushNeedUserNumber(weSopPushTimes.size());
                    //推送所需天数
                    weSopListsVo.setPushNeedDayNumber(weSopPushTimes.stream().collect(Collectors.groupingBy(WeSopPushTime::getPushTimePre)).size());

                }
                weSopListsVos.add(
                        weSopListsVo
                );
            });


        }

        tableDataInfo.setTotal(pageInfo.getTotal());
        tableDataInfo.setRows(weSopListsVos);


        return tableDataInfo;
    }

    @Override
    public WeSopDetailTabVo findWeSopDetailTabVo(String sopBaseId) {
        return this.baseMapper.findWeSopDetailTabVo(sopBaseId);
    }

    @Override
    public  List<WeSopDetailGroupVo> findWeSopDetailGroup(String sopBaseId, String groupName, Integer executeState, String weUserId) {
        return this.baseMapper.findWeSopDetailGroup(sopBaseId, groupName, executeState, weUserId);
    }

    @Override
    public List<WeSopDetailCustomerVo>  findWeSopDetailCustomer(String sopBaseId, String customerName, Integer executeState, String weUserId) {
        return this.baseMapper.findWeSopDetailCustomer(sopBaseId,customerName,executeState,weUserId);
    }

    @Override
    public List<WeCustomerSopContentVo> findCustomerExecuteContent(String executeWeUserId, String targetId, Integer executeSubState, String sopBaseId,String executeTargetId) {
        List<WeCustomerSopContentVo> weCustomerSopContentVos=new ArrayList<>();

        List<WeCustomerSopBaseContentVo> customerExecuteContent
                = this.baseMapper.findCustomerExecuteContent(executeWeUserId, targetId, executeSubState, sopBaseId,executeTargetId,true);
        if(CollectionUtil.isNotEmpty(customerExecuteContent)){


            customerExecuteContent.stream()
                    .collect(Collectors.groupingBy(WeCustomerSopBaseContentVo::getPushTimePre))
                    .forEach((k,v)->{
                        WeCustomerSopContentVo weCustomerSopContentVo=new WeCustomerSopContentVo();
                        v.stream().forEach(vv->{
                            BeanUtils.copyProperties(vv,weCustomerSopContentVo);
                            WeSopAttachments weSopAttachments = iWeSopAttachmentsService.getById(
                                    vv.getSopAttachmentId()
                            );
                            if(null != weSopAttachments){
                                //设置目标与实际推送内容
                                weCustomerSopContentVo.addWeQrAttachments(
                                        weSopAttachments,vv.getExecuteState()
                                );
                            }
                        });


                        //设置已发送天数
                        if(v.stream().filter(v1->v1.getExecuteState()==1).collect(Collectors.counting())>0){
                            weCustomerSopContentVo.setSendDayNumber(1);
                        }


                        //设置已发送次数
                        weCustomerSopContentVo.setSendNumber(
                                v.stream().filter(v1->v1.getExecuteState()==1).collect(Collectors.counting()).intValue()
                        );



                        weCustomerSopContentVos.add(weCustomerSopContentVo);
                    });





        }


        if(CollectionUtil.isNotEmpty(weCustomerSopContentVos)){
            return weCustomerSopContentVos.stream().sorted(Comparator.comparing(WeCustomerSopContentVo::getPushStartTime)).collect(Collectors.toList());
        }

        return weCustomerSopContentVos;
    }


    @Override
    public WeSendCustomerSopContentVo findCustomerSopContent(String executeWeUserId, String targetId, Integer executeSubState){
        WeSendCustomerSopContentVo weSendCustomerSopContentVo=new WeSendCustomerSopContentVo();
        List<WeCustomer> weCustomerList = iWeCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getExternalUserid, targetId)
                .eq(WeCustomer::getAddUserId, executeWeUserId));

        //设置客户相关信息
        if(CollectionUtil.isNotEmpty(weCustomerList)){
            BeanUtils.copyProperties(weCustomerList.stream().findFirst().get(),weSendCustomerSopContentVo);
        }


        List<WeCustomerSopBaseContentVo> customerExecuteContent
                = this.baseMapper.findCustomerExecuteContent(executeWeUserId, targetId, executeSubState, null,null,false);

        if(CollectionUtil.isNotEmpty(customerExecuteContent)){
            List<WeSendCustomerSopContentVo.WeCustomerSop> weCustomerSops =new ArrayList<>();

            customerExecuteContent.stream()
                    .collect(Collectors.groupingBy(WeCustomerSopBaseContentVo::getSopBaseId)).forEach((k,v)->{

                        WeSendCustomerSopContentVo.WeCustomerSop weCustomerSop = WeSendCustomerSopContentVo.WeCustomerSop.builder()
                                .sopBaseId(v.stream().findFirst().get().getSopBaseId())
                                .sopName(v.stream().findFirst().get().getSopName())
                                .build();


                        List<WeSendCustomerSopContentVo.WeCustomerSopContent> weCustomerSopContents =new ArrayList<>();
                        v.stream().forEach(vv->{

                            weCustomerSopContents.add(
                                    WeSendCustomerSopContentVo.WeCustomerSopContent.builder()
                                            .executeState(vv.getExecuteState())
                                            .pushEndTime(vv.getPushEndTime())
                                            .pushStartTime(vv.getPushStartTime())
                                            .weQrAttachments(
                                                    iWeSopAttachmentsService.getById(
                                                            vv.getSopAttachmentId()
                                                    )
                                            )
                                            .executeTargetAttachId(vv.getExecuteTargetAttachId())
                                            .build()
                            );

                        });
                        weCustomerSop.setWeCustomerSopContents(weCustomerSopContents);

                        weCustomerSops.add(weCustomerSop);
                    });


            weSendCustomerSopContentVo.setWeCustomerSops(weCustomerSops);
        }



        return weSendCustomerSopContentVo;

    }


    @Override
    public List<WeGroupSopContentVo> findGroupExecuteContent(String chatId, Integer executeState, String sopBaseId,String executeTargetId) {
        List<WeGroupSopContentVo> weGroupSopContentVos=new ArrayList<>();

        List<WeGroupSopBaseContentVo> groupExecuteContent
                = this.baseMapper.findGroupExecuteContent(chatId, executeState, sopBaseId,executeTargetId,true);
        if(CollectionUtil.isNotEmpty(groupExecuteContent)){
            groupExecuteContent.stream().collect(Collectors.groupingBy(WeGroupSopBaseContentVo::getPushTimePre))
                    .forEach((k,v)->{
                        WeGroupSopContentVo weGroupSopContentVo=new WeGroupSopContentVo();

                        v.stream().forEach(vv->{
                            BeanUtils.copyProperties(vv,weGroupSopContentVo);
                            WeSopAttachments weSopAttachments = iWeSopAttachmentsService.getById(
                                    vv.getSopAttachmentId()
                            );
                            if(null != weSopAttachments){
                                //设置目标与实际推送内容
                                weGroupSopContentVo.addWeQrAttachments(
                                        weSopAttachments,vv.getExecuteState()
                                );
                            }
                        });




                        //设置已发送天数
                        if(v.stream().filter(v1->v1.getExecuteState()==1).collect(Collectors.counting())>0){
                            weGroupSopContentVo.setSendDayNumber(1);
                        }


                        //设置已发送次数
                        weGroupSopContentVo.setSendNumber(
                                v.stream().filter(v1->v1.getExecuteState()==1).collect(Collectors.counting()).intValue()
                        );

                        weGroupSopContentVos.add(weGroupSopContentVo);

                    });
        }

        if(CollectionUtil.isNotEmpty(weGroupSopContentVos)){
            return weGroupSopContentVos.stream().sorted(Comparator.comparing(WeGroupSopContentVo::getPushStartTime)).collect(Collectors.toList());
        }

        return weGroupSopContentVos;
    }


    @Override
    public WeSendGroupSopContentVo findGroupSopContent(String chatId, Integer executeSubState){
        WeSendGroupSopContentVo weSendGroupSopContentVo=new WeSendGroupSopContentVo();

        List<WeGroup> weGroups = iWeGroupService.list(new LambdaQueryWrapper<WeGroup>()
                .eq(WeGroup::getChatId, chatId));
        if(CollectionUtil.isNotEmpty(weGroups)){
            BeanUtils.copyProperties(weGroups.stream().findFirst().get(),weSendGroupSopContentVo);
        }

        List<WeGroupSopBaseContentVo> groupExecuteContent
                = this.baseMapper.findGroupExecuteContent(chatId, executeSubState, null,null,false);

        if(CollectionUtil.isNotEmpty(groupExecuteContent)){
            List<WeSendGroupSopContentVo.WeGroupSop> weGroupSops =new ArrayList<>();

            groupExecuteContent.stream().collect(Collectors.groupingBy(WeGroupSopBaseContentVo::getSopBaseId))
                    .forEach((k,v)->{

                        WeSendGroupSopContentVo.WeGroupSop weGroupSop = WeSendGroupSopContentVo.WeGroupSop.builder()
                                .sopBaseId(v.stream().findFirst().get().getSopBaseId())
                                .sopName(v.stream().findFirst().get().getSopName())
                                .build();

                        List<WeSendGroupSopContentVo.WeGroupSopContent> weGroupSopContents =new ArrayList<>();


                        v.stream().forEach(vv->{

                            weGroupSopContents.add(
                                    WeSendGroupSopContentVo.WeGroupSopContent.builder()
                                            .executeTargetAttachId(vv.getExecuteTargetAttachId())
                                            .executeState(vv.getExecuteState())
                                            .pushEndTime(vv.getPushEndTime())
                                            .pushStartTime(vv.getPushStartTime())
                                            .weQrAttachments(
                                                    iWeSopAttachmentsService.getById(
                                                            vv.getSopAttachmentId()
                                                    )
                                            )
                                            .build()
                            );

                        });

                        weGroupSop.setWeGroupSopContents(weGroupSopContents);

                        weGroupSops.add(weGroupSop);

                    });

            weSendGroupSopContentVo.setWeGroupSops(weGroupSops);
        }




        return weSendGroupSopContentVo;

    }


    @Override
    public List<WeCustomerSopToBeSentVo> findWeCustomerSopToBeSent(boolean isExpiringSoon) {
        List<WeCustomerSopToBeSentVo> sopToBeSentVoList=new ArrayList<>();
        LoginUser loginUser = SecurityUtils.getLoginUser();

        if(null != loginUser && null != loginUser.getSysUser()){




            List<WeCustomerSopToBeSentVo> tdSendSopCustomers
                    = this.baseMapper.findTdSendSopCustomers(loginUser.getSysUser().getWeUserId(),isExpiringSoon);

            if(CollectionUtil.isNotEmpty(tdSendSopCustomers)){


                tdSendSopCustomers.stream()
                        .collect(Collectors.groupingBy(WeCustomerSopToBeSentVo::getExternalUserid)).forEach((k,v)->{
                            WeCustomerSopToBeSentVo weCustomerSopToBeSentVo=new WeCustomerSopToBeSentVo();
                            BeanUtils.copyProperties(v.stream().findFirst().get(),weCustomerSopToBeSentVo);


                            weCustomerSopToBeSentVo.setWeSopToBeSentContentInfoVo(
                                    this.baseMapper.findSopToBeSentContentInfo(loginUser.getSysUser().getWeUserId(),k)
                            );

                            sopToBeSentVoList.add(weCustomerSopToBeSentVo);
                        });

            }


        }

        return sopToBeSentVoList;
    }

    @Override
    public List<WeGroupSopToBeSentVo> findWeGroupSopToBeSent(boolean isExpiringSoon) {
        List<WeGroupSopToBeSentVo> weGroupSopToBeSentVos=new ArrayList<>();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if(null != loginUser && null != loginUser.getSysUser()){



            List<WeGroupSopToBeSentVo> tdSendSopGroups
                    = this.baseMapper.findTdSendSopGroups(loginUser.getSysUser().getWeUserId(),isExpiringSoon);

            if(CollectionUtil.isNotEmpty(tdSendSopGroups)){
                tdSendSopGroups.stream()
                        .collect(Collectors.groupingBy(WeGroupSopToBeSentVo::getChatId)).forEach((k,v)->{
                            WeGroupSopToBeSentVo weGroupSopToBeSentVo=new WeGroupSopToBeSentVo();
                            BeanUtils.copyProperties(v.stream().findFirst().get(),weGroupSopToBeSentVo);

                            weGroupSopToBeSentVo.setWeSopToBeSentContentInfoVo(
                                    this.baseMapper.findSopToBeSentContentInfo(loginUser.getSysUser().getWeUserId(),k)
                            );

                            weGroupSopToBeSentVos.add(weGroupSopToBeSentVo);
                        });
            }



        }
        return weGroupSopToBeSentVos;
    }



    @Override
    public void executeSop(String executeTargetAttachId) {

        WeSopExecuteTargetAttachments targetAttachments
                = iWeSopExecuteTargetAttachmentsService.getById(executeTargetAttachId);
        if(null != targetAttachments){
            targetAttachments.setExecuteTime(new Date());
            targetAttachments.setExecuteState(1);
            targetAttachments.setIsPushOnTime(
                    DateUtils.isEffectiveDate(new Date(),targetAttachments.getPushStartTime(),targetAttachments.getPushEndTime())?0:1
            );

            iWeSopExecuteTargetAttachmentsService.updateById(
                    targetAttachments
            );

            //检索当前sop下当前客户有没有需要检索的
            if(iWeSopExecuteTargetAttachmentsService.count(new LambdaQueryWrapper<WeSopExecuteTargetAttachments>()
                    .eq(WeSopExecuteTargetAttachments::getExecuteTargetId,targetAttachments.getExecuteTargetId())
                    .eq(WeSopExecuteTargetAttachments::getExecuteState,0)
            )==0){ //结束任务同时执行相关sop结束动作

                executeTargetService.sopExecuteEndAction(
                        targetAttachments.getExecuteTargetId()
                );


            }


        }

    }

    @Override
    public Map<String, List<LinkGroupChatListVo>> builderExecuteGroup(WeSopBase weSopBase,WeSopExecuteConditVo executeCustomerOrGroup, Set<String> executeWeUserIds) {
        Map<String,List<LinkGroupChatListVo>> weGroupMap=new HashMap<>();


        WeGroupChatQuery weGroupChatQuery = WeGroupChatQuery.builder()
                .userIds(StringUtils.join(executeWeUserIds, ","))
                .build();
        //查询全部群
        if(Objects.nonNull(executeCustomerOrGroup)){

            //时间
            WeSopExecuteConditVo.WeSopExecuteGroupTimeCondit weSopExecuteGroupTimeCondit
                    = executeCustomerOrGroup.getWeSopExecuteGroupTimeCondit();
            if(Objects.nonNull(weSopExecuteGroupTimeCondit)&&weSopExecuteGroupTimeCondit.isChange()){
                weGroupChatQuery.setBeginTime( DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, weSopExecuteGroupTimeCondit.getBeginTime()));
                weGroupChatQuery.setEndTime( DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, weSopExecuteGroupTimeCondit.getEndTime()));
            }

            //标签
            WeSopExecuteConditVo.WeSopExecuteGroupTagIdsCondit weSopExecuteGroupTagIdsCondit
                    = executeCustomerOrGroup.getWeSopExecuteGroupTagIdsCondit();
            if(Objects.nonNull(weSopExecuteGroupTagIdsCondit)&&weSopExecuteGroupTagIdsCondit.isChange()){
                weGroupChatQuery.setTagIds(StringUtils.join(weSopExecuteGroupTagIdsCondit.getTagIds(),","));
            }

            //上线
            WeSopExecuteConditVo.WeSopExecuteGroupMemberLimitCondit weSopExecuteGroupMemberLimitCondit
                    = executeCustomerOrGroup.getWeSopExecuteGroupMemberLimitCondit();
            if(Objects.nonNull(weSopExecuteGroupMemberLimitCondit)&&weSopExecuteGroupMemberLimitCondit.isChange()){
                weGroupChatQuery.setGroupMemberUp(weSopExecuteGroupMemberLimitCondit.getGroupMemberUp());
                weGroupChatQuery.setGroupMemberDown(weSopExecuteGroupMemberLimitCondit.getGroupMemberDown());
            }

        }
        List<LinkGroupChatListVo> weGroups =  iWeGroupService.getPageList(weGroupChatQuery);
        if(CollectionUtil.isNotEmpty(weGroups)){

            if (weSopBase.getBusinessType().equals(SopType.SOP_TYPE_XQPY.getSopKey())) {

                weGroups =  weGroups.stream().filter(LinkGroupChatListVo ->
                        DateUtils.parseDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, weSopBase.getCreateTime())).getTime()
                                <= DateUtils.parseDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, LinkGroupChatListVo.getAddTime())).getTime()
                ).collect(Collectors.toList());

            }


            weGroupMap.putAll(
                    weGroups.stream().collect(Collectors.groupingBy(LinkGroupChatListVo::getOwner))
            );
        }



        return weGroupMap;
    }



    @Override
    public Set<String> builderExecuteWeUserIds(WeSopExecuteUserConditVo executeWeUser) {
        //生成执行任务
        Set<String> executeWeUserIds=new HashSet<>();

        //1.查询执行成员
        if(Objects.isNull(executeWeUser)){//全部成员
            AjaxResult<List<SysUser>> listAjaxResult = qwSysUserClient.list(new SysUser());

            if(null != listAjaxResult&&CollectionUtil.isNotEmpty(listAjaxResult.getData())){
                executeWeUserIds.addAll(
                        listAjaxResult.getData().stream().map(SysUser::getWeUserId).collect(Collectors.toList())
                );
            }

        }else{ //部分成员
            //用户选择的成员
            WeSopExecuteUserConditVo.ExecuteUserCondit executeUserCondit = executeWeUser.getExecuteUserCondit();
            if(null != executeUserCondit && executeUserCondit.isChange()){
                executeWeUserIds.addAll(
                        executeUserCondit.getWeUserIds()
                );
            }

            //按照部门与岗位筛选
            WeSopExecuteUserConditVo.ExecuteDeptCondit executeDeptCondit = executeWeUser.getExecuteDeptCondit();
            if(null != executeDeptCondit && executeDeptCondit.isChange()){
                String positions = null,depteIds=null;
                if(CollectionUtil.isNotEmpty(executeDeptCondit.getDeptIds())){
                    depteIds=StringUtils.join(executeDeptCondit.getDeptIds(),",");
                }

                if(CollectionUtil.isNotEmpty(executeDeptCondit.getPosts())){
                    positions=StringUtils.join(executeDeptCondit.getPosts(),",");
                }


                AjaxResult<List<SysUser>> listAjaxResult =qwSysUserClient.findAllSysUser("",positions,depteIds);

                if(null != listAjaxResult&&CollectionUtil.isNotEmpty(listAjaxResult.getData())){
                    executeWeUserIds.addAll(
                            listAjaxResult.getData().stream().map(SysUser::getWeUserId).collect(Collectors.toList())
                    );
                }
//                if(CollectionUtil.isNotEmpty(allSysUser)){
//
//                    executeWeUserIds.addAll(
//                            allSysUser.stream().map(SysUser::getWeUserId).collect(Collectors.toList())
//                    );
//                }

            }

        }

        return executeWeUserIds;

    }

    @Override
    public void builderExecuteCustomerSopPlan(WeSopBase weSopBase, Map<String, List<WeCustomer>> executeWeCustomers, boolean isCreateOrUpdate,boolean buildXkSopPlan) {
        if(CollectionUtil.isNotEmpty(executeWeCustomers)){
            List<WeSopExecuteTarget> weSopExecuteTargets=new ArrayList<>();
            executeWeCustomers.forEach((k,v)->{
                v.stream().forEach(weCustomer -> {
                    weSopExecuteTargets.add(
                            WeSopExecuteTarget.builder()
                                    .targetId(weCustomer.getExternalUserid())
                                    .targetType(1)
                                    .addCustomerOrCreateGoupTime(weCustomer.getAddTime())
                                    .sopBaseId(weSopBase.getId())
                                    .executeWeUserId(k)
                                    .build()
                    );
                });
            });

            if(CollectionUtil.isNotEmpty(weSopExecuteTargets)){

                //处理不满足当前条件的生效客户(针对客户sop编辑)
                if(!isCreateOrUpdate){
                    executeTargetService.editSopExceptionEnd(weSopBase.getId(),
                            weSopExecuteTargets.stream().map(WeSopExecuteTarget::getTargetId).collect(Collectors.toList()));
                }


                if(executeTargetService.saveOrUpdateBatch(weSopExecuteTargets)){

                    List<WeSopPushTimeDto> weSopPushTimeDtos
                            = ((WeSopPushTimeMapper) iWeSopPushTimeService.getBaseMapper()).findWeSopPushTimeDto(weSopBase.getId());
                    if(CollectionUtil.isNotEmpty(weSopPushTimeDtos)){
                        List<WeSopExecuteTargetAttachments> targetAttachments=new ArrayList<>();

                        weSopExecuteTargets.stream().forEach(weSopExecuteTarget -> {


                            weSopPushTimeDtos.stream().forEach(weSopPushTimeDto -> {

                                WeSopExecuteTargetAttachments attachments = WeSopExecuteTargetAttachments.builder()
                                        .executeTargetId(weSopExecuteTarget.getId())
                                        .sopAttachmentId(weSopPushTimeDto.getSopAttachmentId())
                                        .pushTimePre(weSopPushTimeDto.getPushTimePre())
                                        .pushTimeType(weSopPushTimeDto.getPushTimeType())
                                        .build();
                                if(weSopPushTimeDto.getPushTimeType()==3){//设置推送时间（相对推送时间）员工添加客户的时间启始时间

                                    if(weSopPushTimeDto.getPushTimePre().matches("^[0-9]*$")){
                                        //推送日期
                                        String pushDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.daysAgoOrAfter(weSopExecuteTarget.getAddCustomerOrCreateGoupTime()
                                                , new Integer(weSopPushTimeDto.getPushTimePre())));

                                        attachments.setPushStartTime(
                                                DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,pushDate+" "+weSopPushTimeDto.getPushStartTime())
                                        );

                                        attachments.setPushEndTime(
                                                DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,pushDate+" "+weSopPushTimeDto.getPushEndTime())
                                        );
                                    }
                                }else if(weSopPushTimeDto.getPushTimeType()==1){//具体推送时间
                                    attachments.setPushStartTime(
                                            DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,weSopPushTimeDto.getPushTimePre()+" "+weSopPushTimeDto.getPushStartTime()));
                                    attachments.setPushEndTime(
                                            DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,weSopPushTimeDto.getPushTimePre()+" "+weSopPushTimeDto.getPushEndTime())
                                    );
                                }


                                if(weSopBase.getBusinessType().equals(SopType.SOP_TYPE_XK.getSopKey())){
                                    if(buildXkSopPlan){
                                        targetAttachments.add(attachments);
                                    }
                                }else{
                                    targetAttachments.add(attachments);
                                }


                            });


                        });

                        if(CollectionUtil.isNotEmpty(targetAttachments)){




                            iWeSopExecuteTargetAttachmentsService.saveOrUpdateBatch(targetAttachments);

                            Set<Long> sopExecuteTargetIds = weSopExecuteTargets.stream().map(WeSopExecuteTarget::getId).collect(Collectors.toSet());

                            if(sopExecuteTargetIds.removeAll(
                                    targetAttachments.stream().map(WeSopExecuteTargetAttachments::getExecuteTargetId).collect(Collectors.toSet()))){


                                executeTargetService.removeByIds(
                                        sopExecuteTargetIds
                                );



                            }

                        }else{//为空,则删除当前所有不满足条件的执行目标
                            executeTargetService.removeByIds(
                                    weSopExecuteTargets.stream().map(WeSopExecuteTarget::getId).collect(Collectors.toSet())
                            );

                        }


                    }






                }


            }
        }else{

            //处理不满足当前条件的生效客群(针对客客户sop编辑)
            executeTargetService.editSopExceptionEnd(weSopBase.getId(),
                    new ArrayList<>());
        }
    }

    @Override
    public void builderExecuteGroupSopPlan(WeSopBase weSopBase, Map<String, List<LinkGroupChatListVo>> executeGroups, boolean isCreateOrUpdate) {
        if(CollectionUtil.isNotEmpty(executeGroups)){


            List<WeSopExecuteTarget> weSopExecuteTargets=new ArrayList<>();
            executeGroups.forEach((k,v)->{
                v.stream().forEach(group -> {
                    WeSopExecuteTarget weSopExecuteTarget = WeSopExecuteTarget.builder()
                            .targetId(group.getChatId())
                            .targetType(2)
                            .addCustomerOrCreateGoupTime(group.getAddTime())
                            .sopBaseId(weSopBase.getId())
                            .executeWeUserId(k)
                            .build();

                    weSopExecuteTargets.add(
                            weSopExecuteTarget
                    );
                });
            });


            if(CollectionUtil.isNotEmpty(weSopExecuteTargets)) {

                if(!isCreateOrUpdate){
                    //处理不满足当前条件的生效客群(针对客群sop编辑)
                    executeTargetService.editSopExceptionEnd(weSopBase.getId(),
                            weSopExecuteTargets.stream().map(WeSopExecuteTarget::getTargetId).collect(Collectors.toList()));
                }

                if (executeTargetService.saveOrUpdateBatch(weSopExecuteTargets)) {

                    List<WeSopPushTimeDto> weSopPushTimeDtos
                            = ((WeSopPushTimeMapper) iWeSopPushTimeService.getBaseMapper()).findWeSopPushTimeDto(weSopBase.getId());
                    if(CollectionUtil.isNotEmpty(weSopPushTimeDtos)){

                        List<WeSopExecuteTargetAttachments> targetAttachments=new ArrayList<>();
                        weSopExecuteTargets.stream().forEach(weSopExecuteTarget -> {

                            weSopPushTimeDtos.stream().forEach(weSopPushTimeDto -> {

                                WeSopExecuteTargetAttachments attachments = WeSopExecuteTargetAttachments.builder()
                                        .executeTargetId(weSopExecuteTarget.getId())
                                        .sopAttachmentId(weSopPushTimeDto.getSopAttachmentId())
                                        .pushTimePre(weSopPushTimeDto.getPushTimePre())
                                        .pushTimeType(weSopPushTimeDto.getPushTimeType())
                                        .build();


                                if(weSopPushTimeDto.getPushTimeType()==2) {//设置推送时间,周期推送，先计算出当前时间至周日下的推送,后续客每周日运行定时任务生成下一周的执行计划
                                    //获取当天是周几
//                                    int currentWeek = DateUtil.dayOfWeek(new Date());
//
//                                    if(currentWeek-1<=new Integer(weSopPushTimeDto.getPushTimePre())){//生成符合条件当前日期下到周日的具体时间
                                        //执行日期
                                        String executeData
                                                = WeekDateUtils.GetCurrentWeekAllDate().get(Integer.parseInt(weSopPushTimeDto.getPushTimePre()));

                                        if(StringUtils.isNotEmpty(executeData)){
                                            Date pushEndTime
                                                    = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, executeData + " " + weSopPushTimeDto.getPushEndTime());

                                                    if(new Date().before(
                                                            pushEndTime
                                                    )){

                                                        attachments.setPushStartTime(
                                                                DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,executeData+" "+weSopPushTimeDto.getPushStartTime())
                                                        );

                                                        attachments.setPushEndTime(
                                                                pushEndTime
                                                        );


                                                    }

                                        }

//                                    }




                                }else if(weSopPushTimeDto.getPushTimeType()==1){//具体推送时间
                                    attachments.setPushStartTime(
                                            DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,weSopPushTimeDto.getPushTimePre()+" "+weSopPushTimeDto.getPushStartTime()));
                                    attachments.setPushEndTime(
                                            DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,weSopPushTimeDto.getPushTimePre()+" "+weSopPushTimeDto.getPushEndTime())
                                    );
                                }else if(weSopPushTimeDto.getPushTimeType()==3){ //新群
                                  if(weSopBase.getCreateTime()
                                          .before(weSopExecuteTarget.getAddTime())){

                                      if(weSopPushTimeDto.getPushTimePre().matches("^[0-9]*$")){
                                          //推送日期
                                          String pushDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.daysAgoOrAfter(weSopExecuteTarget.getAddCustomerOrCreateGoupTime()
                                                  , new Integer(weSopPushTimeDto.getPushTimePre())));

                                          attachments.setPushStartTime(
                                                  DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,pushDate+" "+weSopPushTimeDto.getPushStartTime())
                                          );

                                          attachments.setPushEndTime(
                                                  DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,pushDate+" "+weSopPushTimeDto.getPushEndTime())
                                          );
                                      }

                                  }



                                }


                                if(attachments.getPushStartTime() != null && attachments.getPushEndTime() != null){
                                    targetAttachments.add(attachments);
                                }


                            });


                        });

                        if(CollectionUtil.isNotEmpty(targetAttachments)){
                            iWeSopExecuteTargetAttachmentsService.saveOrUpdateBatch(targetAttachments);
                        }
                        //移除不存在任务的执行客户
                        List<WeSopExecuteTargetAttachments> sopExecuteTargetAttachments = iWeSopExecuteTargetAttachmentsService.list(new LambdaQueryWrapper<WeSopExecuteTargetAttachments>()
                                .in(WeSopExecuteTargetAttachments::getExecuteTargetId
                                        , weSopExecuteTargets.stream().map(WeSopExecuteTarget::getId).collect(Collectors.toList())));
                        if(CollectionUtil.isNotEmpty(sopExecuteTargetAttachments)){
                            executeTargetService.remove(new LambdaQueryWrapper<WeSopExecuteTarget>()
                                    .eq(WeSopExecuteTarget::getSopBaseId,weSopBase.getId())
                                    .notIn(WeSopExecuteTarget::getId,sopExecuteTargetAttachments
                                            .stream().map(WeSopExecuteTargetAttachments::getExecuteTargetId).collect(Collectors.toList())));
                        }else{
                            executeTargetService.removeByIds(weSopExecuteTargets.stream().map(WeSopExecuteTarget::getId).collect(Collectors.toList()));
                        }


                    }



                }
            }



        }else{

            //处理不满足当前条件的生效客群(针对客群sop编辑)
            executeTargetService.editSopExceptionEnd(weSopBase.getId(),
                    new ArrayList<>());
        }
    }



    @Override
    public void synchSopExecuteResultForWeChatPushType(String sopBaseId) {

        List<WeSopExecuteTarget> weSopExecuteTargets = executeTargetService.list(new LambdaQueryWrapper<WeSopExecuteTarget>()
                .eq(WeSopExecuteTarget::getSopBaseId, sopBaseId)
                .eq(WeSopExecuteTarget::getDelFlag,Constants.COMMON_STATE)
                .eq(WeSopExecuteTarget::getExecuteState, SopExecuteStatus.SOP_STATUS_ING.getType()));
        if(CollectionUtil.isNotEmpty(weSopExecuteTargets)){

            //获取具体执行计划
            List<WeSopExecuteTargetAttachments> targetAttachments = iWeSopExecuteTargetAttachmentsService.list(new LambdaQueryWrapper<WeSopExecuteTargetAttachments>()
                    .eq(WeSopExecuteTargetAttachments::getDelFlag, Constants.COMMON_STATE)
                    .isNotNull(WeSopExecuteTargetAttachments::getMsgId)
                    .eq(WeSopExecuteTargetAttachments::getExecuteState, 0)
                    .in(WeSopExecuteTargetAttachments::getExecuteTargetId,
                            weSopExecuteTargets.stream().map(WeSopExecuteTarget::getId).collect(Collectors.toList())));

            if(CollectionUtil.isNotEmpty(targetAttachments)){
                Map<Long, List<WeSopExecuteTargetAttachments>> targetAttachmentMap
                        = targetAttachments.stream().collect(Collectors.groupingBy(WeSopExecuteTargetAttachments::getExecuteTargetId));

                weSopExecuteTargets.stream().forEach(k->{
                    List<WeSopExecuteTargetAttachments> sopExecuteTargetAttachment = targetAttachmentMap.get(k.getId());
                    if(CollectionUtil.isNotEmpty(sopExecuteTargetAttachment) ){

                        sopExecuteTargetAttachment.stream().forEach(kk->{

                            WeGetGroupMsgListQuery listQuery=new WeGetGroupMsgListQuery();
                            listQuery.setMsgid(kk.getMsgId());
                            listQuery.setUserid(k.getExecuteWeUserId());

                            WeGroupMsgListVo groupMsgSendResult = qwCustomerClient.getGroupMsgSendResult(listQuery).getData();
                            Optional.ofNullable(groupMsgSendResult).map(WeGroupMsgListVo::getSendList).orElseGet(ArrayList::new).forEach(sendResult -> {
                                //设置发送时间
                                if(null != sendResult.getSendTime()){
                                    kk.setExecuteTime(new Date(sendResult.getSendTime()*1000L));
                                }
                                //设置任务执行状态
                                if(sendResult.getStatus()!=0){
                                    kk.setExecuteState(1);
                                }
                            });
                        });

                        iWeSopExecuteTargetAttachmentsService.updateBatchById(sopExecuteTargetAttachment);


                        //校验如果全部任务都执行完，全局状态设置为完成
                        if(iWeSopExecuteTargetAttachmentsService.count(new LambdaQueryWrapper<WeSopExecuteTargetAttachments>()
                                .eq(WeSopExecuteTargetAttachments::getExecuteTargetId,k.getId())
                                .eq(WeSopExecuteTargetAttachments::getExecuteState,0)
                                .eq(WeSopExecuteTargetAttachments::getDelFlag,Constants.COMMON_STATE))==0){
                            k.setExecuteEndTime(new Date());
                            k.setExecuteState(SopExecuteStatus.SOP_STATUS_COMMON.getType());
                        }

                    }





                });

                executeTargetService.updateBatchById(weSopExecuteTargets);
            }


        }

    }



    @Override
    public void updateSopState(String sopId, Integer sopState) {
        this.baseMapper.updateSopState(sopId,sopState);
    }


}

