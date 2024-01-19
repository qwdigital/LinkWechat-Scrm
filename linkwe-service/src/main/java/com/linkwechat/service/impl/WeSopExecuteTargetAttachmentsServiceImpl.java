package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.MessageNoticeType;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.sop.WeSopAttachments;
import com.linkwechat.domain.sop.WeSopExecuteTargetAttachments;
import com.linkwechat.domain.sop.dto.WeSopPushTaskDto;
import com.linkwechat.domain.task.query.WeTasksRequest;
import com.linkwechat.mapper.WeCustomerMapper;
import com.linkwechat.mapper.WeGroupMapper;
import com.linkwechat.mapper.WeSopExecuteTargetAttachmentsMapper;
import com.linkwechat.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.SealedObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author robin
* @description 针对表【we_sop_execute_target_attachments(目标执行内容)】的数据库操作Service实现
* @createDate 2022-09-13 16:26:00
*/
@Service
@SuppressWarnings("all")
public class WeSopExecuteTargetAttachmentsServiceImpl extends ServiceImpl<WeSopExecuteTargetAttachmentsMapper, WeSopExecuteTargetAttachments>
implements IWeSopExecuteTargetAttachmentsService {

    @Autowired
    private IWeSopAttachmentsService iWeSopAttachmentsService;

    @Autowired
    private IWeMessagePushService iWeMessagePushService;

    @Autowired
    private IWeGroupService iWeGroupService;

    @Autowired
    private IWeCustomerService iWeCustomerService;

    @Autowired
    private IWeCorpAccountService iWeCorpAccountService;

    @Resource
    private IWeTasksService weTasksService;






    @Override
    public void weChatPushTypeSopTaskTip(String sopBaseId) {
        //获取当天要推送的所有任务
        List<WeSopPushTaskDto> weCustomerSopPushTaskDto
                =this.baseMapper.findWeSopPushTaskDtoBySopId(sopBaseId);

        if(CollectionUtil.isNotEmpty(weCustomerSopPushTaskDto)){

            //获取企业微信发送方式任务
            this.weComPushTask(weCustomerSopPushTaskDto);

        }


    }



    private void weComPushTask(List<WeSopPushTaskDto> wecomSendTypes) {

        WeCorpAccount weCorpAccount = iWeCorpAccountService.getCorpAccountByCorpId(null);

        if(CollectionUtil.isNotEmpty(wecomSendTypes) && ObjectUtil.isNotEmpty(weCorpAccount)) {
            //员工id分组
            Map<String, List<WeSopPushTaskDto>> executeWeUserIdGroup
                    = wecomSendTypes.stream().collect(Collectors.groupingBy(WeSopPushTaskDto::getExecuteWeUserId));

            if (CollectionUtil.isNotEmpty(executeWeUserIdGroup)) {

                executeWeUserIdGroup.forEach((k, vv) -> {
                    WeAddGroupMessageQuery messageQuery = new WeAddGroupMessageQuery();
                    messageQuery.setMsgSource(2);
                    messageQuery.setBusinessIds(
                            String.join(",",vv.stream().map(WeSopPushTaskDto::getExcuteTargetAttachId).collect(Collectors.toList()))
                    );
                    messageQuery.setAll(false);
                    messageQuery.setIsTask(0);
                    LoginUser loginUser=new LoginUser();
                    loginUser.setUserName(weCorpAccount.getCreateBy());
                    loginUser.setCorpId(weCorpAccount.getCorpId());
                    loginUser.setSysUser(SysUser.builder()
                            .build());
                    messageQuery.setLoginUser(loginUser);
                    messageQuery.setAttachmentsList(
                            iWeSopAttachmentsService.weSopAttachmentsToTemplate(
                                    iWeSopAttachmentsService.listByIds(vv.stream().map(WeSopPushTaskDto::getSopAttachmentId).collect(Collectors.toList()))
                            )
                    );

                    List<WeAddGroupMessageQuery.SenderInfo> senderInfos = new ArrayList<>();

                    WeAddGroupMessageQuery.SenderInfo senderInfo = WeAddGroupMessageQuery.SenderInfo.builder()
                            .userId(k)
                            .build();
                    List<WeSopPushTaskDto> weSopPushCustomerTaskDtos = vv.stream().filter(weSopPushTaskDto ->
                            weSopPushTaskDto.getTargetType() == 1).collect(Collectors.toList());

                    if (CollectionUtil.isNotEmpty(weSopPushCustomerTaskDtos)) {
                        messageQuery.setChatType(1);
                        senderInfo.setCustomerList(
                                weSopPushCustomerTaskDtos.stream().map(WeSopPushTaskDto::getTargetId).collect(Collectors.toList())
                        );
                    }

                    List<WeSopPushTaskDto> weSopPushGroupTaskDtos = vv.stream().filter(weSopPushTaskDto ->
                            weSopPushTaskDto.getTargetType() == 2).collect(Collectors.toList());

                    if (CollectionUtil.isNotEmpty(weSopPushGroupTaskDtos)) {
                        messageQuery.setChatType(2);
                        senderInfo.setChatList(
                                weSopPushGroupTaskDtos.stream().map(WeSopPushTaskDto::getTargetId).collect(Collectors.toList())
                        );
                    }



                    senderInfos.add(senderInfo);
//                        messageQuery.setSendTime(kk);

                    messageQuery.setSenderList(senderInfos);

                    //通知用户群发
                    iWeMessagePushService.officialPushMessage(messageQuery);




                    //时间分组
//                    Map<Date, List<WeSopPushTaskDto>> pushTasks
//                            = v.stream().collect(Collectors.groupingBy(WeSopPushTaskDto::getPushEndTime));
//                    pushTasks.forEach((kk, vv) -> {
//                        WeAddGroupMessageQuery messageQuery = new WeAddGroupMessageQuery();
//                        messageQuery.setMsgSource(2);
//                        messageQuery.setBusinessIds(
//                                String.join(",",vv.stream().map(WeSopPushTaskDto::getExcuteTargetAttachId).collect(Collectors.toList()))
//                        );
//                        messageQuery.setIsAll(false);
//                        messageQuery.setIsTask(0);
//                        LoginUser loginUser=new LoginUser();
//                        loginUser.setUserName(weCorpAccount.getCreateBy());
//                        loginUser.setCorpId(weCorpAccount.getCorpId());
//                        loginUser.setSysUser(SysUser.builder()
//                                .build());
//                        messageQuery.setLoginUser(loginUser);
//                        messageQuery.setAttachmentsList(
//                                iWeSopAttachmentsService.weSopAttachmentsToTemplate(
//                                        iWeSopAttachmentsService.listByIds(vv.stream().map(WeSopPushTaskDto::getSopAttachmentId).collect(Collectors.toList()))
//                                )
//                        );
//
//                        List<WeAddGroupMessageQuery.SenderInfo> senderInfos = new ArrayList<>();
//
//                        WeAddGroupMessageQuery.SenderInfo senderInfo = WeAddGroupMessageQuery.SenderInfo.builder()
//                                .userId(k)
//                                .build();
//                        List<WeSopPushTaskDto> weSopPushCustomerTaskDtos = vv.stream().filter(weSopPushTaskDto ->
//                                weSopPushTaskDto.getTargetType() == 1).collect(Collectors.toList());
//
//                        if (CollectionUtil.isNotEmpty(weSopPushCustomerTaskDtos)) {
//                            messageQuery.setChatType(1);
//                            senderInfo.setCustomerList(
//                                    weSopPushCustomerTaskDtos.stream().map(WeSopPushTaskDto::getTargetId).collect(Collectors.toList())
//                            );
//                        }
//
//                        List<WeSopPushTaskDto> weSopPushGroupTaskDtos = vv.stream().filter(weSopPushTaskDto ->
//                                weSopPushTaskDto.getTargetType() == 2).collect(Collectors.toList());
//
//                        if (CollectionUtil.isNotEmpty(weSopPushGroupTaskDtos)) {
//                            messageQuery.setChatType(2);
//                            senderInfo.setChatList(
//                                    weSopPushGroupTaskDtos.stream().map(WeSopPushTaskDto::getTargetId).collect(Collectors.toList())
//                            );
//                        }
//
//
//
//                        senderInfos.add(senderInfo);
////                        messageQuery.setSendTime(kk);
//
//                        messageQuery.setSenderList(senderInfos);
//
//                        //通知用户群发
//                        iWeMessagePushService.officialPushMessage(messageQuery);
//
//
//                    });


                });

            }
        }

    }


    @Override
    public void manualPushTypeSopTaskTip(boolean isExpiringSoon){

        //获取当天要推送的所有任务
        List<WeSopPushTaskDto> weCustomerSopPushTaskDto
                = this.baseMapper.findWeSopPushTaskDto(null,2,isExpiringSoon);

        if(CollectionUtil.isNotEmpty(weCustomerSopPushTaskDto)){

            //每个执行人员
            weCustomerSopPushTaskDto.stream().collect(Collectors.groupingBy(WeSopPushTaskDto::getExecuteWeUserId)).forEach((kk,vv)->{

                Map<Integer, List<WeSopPushTaskDto>> collect = vv.stream().collect(Collectors.groupingBy(WeSopPushTaskDto::getTargetType));

                //客户sop手动推送任务
                this.sopTaskTodayTip(kk,false, collect.get(1),isExpiringSoon);

                //客群sop手动推送任务
                this.sopTaskTodayTip(kk,true,collect.get(2),isExpiringSoon);
            });


        }

    }

    @Override
    public List<WeSopPushTaskDto> findWeSopPushTaskDtoByWeUserId(String weUserId, Integer targetType, Integer sendType) {
        return this.baseMapper.findWeSopPushTaskDtoByWeUserId(weUserId,targetType,sendType);
    }


    private void sopTaskTodayTip(String executeWeUserId,boolean groupOrCustomer,List<WeSopPushTaskDto> weCustomerSopPushTaskDto,boolean isExpiringSoon){

        if(CollectionUtil.isNotEmpty(weCustomerSopPushTaskDto)) {
            StringBuilder textContent = new StringBuilder();
            if(groupOrCustomer){
                textContent.append("【客群SOP】\r\n");

                if(isExpiringSoon){
                    textContent.append("以下客群的SOP推送时间剩余10分钟。\r\n");
                }else{

                    textContent.append(" 今天有" +
                            weCustomerSopPushTaskDto.stream().map(WeSopPushTaskDto::getTargetId).collect(Collectors.toSet()).size()
                            + "个客群SOP待推送。\r\n");
                }


            }else{
                textContent.append("【客户SOP】\r\n");
                if(isExpiringSoon){
                  textContent.append("以下客户的SOP推送时间剩余10分钟。\r\n");
                }else{
                    textContent.append(" 今天有" +
                            weCustomerSopPushTaskDto.stream().map(WeSopPushTaskDto::getTargetId).collect(Collectors.toSet()).size()
                            + "个客户SOP待推送。\r\n");
                }

            }



//            Map<String, List<WeSopPushTaskDto>> weSopPushTaskDtos
//                    = weCustomerSopPushTaskDto.stream().collect(Collectors.groupingBy(WeSopPushTaskDto::getExecuteWeUserId));
//            if (CollectionUtil.isNotEmpty(weSopPushTaskDtos)) {


                if(groupOrCustomer){
//
//
//                    List<WeGroup> weGroups = ((WeGroupMapper)iWeGroupService.getBaseMapper()).selectList(
//                            new LambdaQueryWrapper<WeGroup>()
//                                    .in(WeGroup::getChatId, weCustomerSopPushTaskDto.stream().map(WeSopPushTaskDto::getTargetId).collect(Collectors.toList()))
//                                    .last("limit 10")
//                    );
//
//                    if(CollectionUtil.isNotEmpty(weGroups)){
//
//                        weGroups.stream().forEach(weGroup->{
//
//                            WeSopPushTaskDto weSopPushTaskDtoStream =
//                                    weCustomerSopPushTaskDto.stream().filter(task -> task.getTargetId().equals(weGroup.getChatId()) && task.getExecuteWeUserId().equals(executeWeUserId))
//                                            .collect(Collectors.toList()).stream().findFirst().get();
//
//                            if(null !=weSopPushTaskDtoStream){
//                                if(isExpiringSoon){
//                                    textContent.append("【"+weGroup.getGroupName()+"】\r\n");
//                                }else{
//                                    textContent.append("【"+weGroup.getGroupName()+"】"+
//                                            DateUtil.format(weSopPushTaskDtoStream.getPushStartTime(),"HH:mm:ss")+"——"+DateUtil.format(weSopPushTaskDtoStream.getPushEndTime(),"HH:mm:ss")+"\r\n");
//
//                                }
//
//                            }
//
//                        });

                        if(isExpiringSoon){
                            textContent.append(" 请注意及时推送。");
                            iWeMessagePushService.pushMessageSelfH5(
                                    ListUtil.toList(executeWeUserId),textContent.toString(),groupOrCustomer?MessageNoticeType.GROUP_SOP_DQTX.getType():MessageNoticeType.CUSTOMER_SOP_DQTX.getType(),true
                            );

                        }else{
                            iWeMessagePushService.pushMessageSelfH5(
                                    ListUtil.toList(executeWeUserId),textContent.toString(),groupOrCustomer?MessageNoticeType.GROUP_SOP.getType():MessageNoticeType.CUSTOMER_SOP.getType(),true
                            );

                            //客群SOP代办任务
                            WeTasksRequest build = WeTasksRequest.builder().weUserId(executeWeUserId).content(textContent.toString()).build();
                            weTasksService.addGroupSop(build);

                        }



                }else{


//                    List<WeCustomer> weCustomers =((WeCustomerMapper)iWeCustomerService.getBaseMapper()).selectList(
//                            new LambdaQueryWrapper<WeCustomer>()
//                                    .eq(WeCustomer::getAddUserId,executeWeUserId)
//                                    .in(WeCustomer::getExternalUserid, weCustomerSopPushTaskDto.stream().map(WeSopPushTaskDto::getTargetId).collect(Collectors.toList()))
//                                    .last("limit 10")
//                    );
//
//
//                    if(CollectionUtil.isNotEmpty(weCustomers)){
//
//                        weCustomers.stream().forEach(kk->{
//                            WeSopPushTaskDto weSopPushTaskDtoStream =
//                                    weCustomerSopPushTaskDto.stream().filter(task -> task.getTargetId().equals(kk.getExternalUserid()) && task.getExecuteWeUserId().equals(executeWeUserId))
//                                            .collect(Collectors.toList()).stream().findFirst().get();
//
//                            if(null != weSopPushTaskDtoStream){
//                                if(isExpiringSoon){
//                                    textContent.append("【"+kk.getCustomerName()+"】\r\n");
//                                }else{
//                                    textContent.append("【"+kk.getCustomerName()+"】"+
//                                            DateUtil.format(weSopPushTaskDtoStream.getPushStartTime(),"HH:mm:ss")+"——"+DateUtil.format(weSopPushTaskDtoStream.getPushEndTime(),"HH:mm:ss")+"\r\n");
//
//                                }
//
//                            }
//
//                        });

                        if(isExpiringSoon){
                            textContent.append(" 请注意及时推送。");
                            iWeMessagePushService.pushMessageSelfH5(
                                    ListUtil.toList(executeWeUserId),textContent.toString(),groupOrCustomer?MessageNoticeType.GROUP_SOP_DQTX.getType():MessageNoticeType.CUSTOMER_SOP_DQTX.getType(),true
                            );
                        }else{
                            iWeMessagePushService.pushMessageSelfH5(
                                    ListUtil.toList(executeWeUserId),textContent.toString(),groupOrCustomer?MessageNoticeType.GROUP_SOP.getType():MessageNoticeType.CUSTOMER_SOP.getType(),true
                            );

                            //客户SOP代办任务
                            WeTasksRequest build = WeTasksRequest.builder().weUserId(executeWeUserId).content(textContent.toString()).build();
                            weTasksService.addCustomerSop(build);

                        }



//                }


                //设置为已提示
                this.update(
                        WeSopExecuteTargetAttachments.builder()
                                .isTip(isExpiringSoon?2:1)
                                .build()
                         ,new LambdaQueryWrapper<WeSopExecuteTargetAttachments>()
                         .in(WeSopExecuteTargetAttachments::getId,weCustomerSopPushTaskDto.stream().map(WeSopPushTaskDto::getExcuteTargetAttachId).collect(Collectors.toList())));

            }
        }

    }


}
