package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.*;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.groupmsg.vo.WeGroupMessageDetailVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.domain.wecom.query.customer.msg.WeGetGroupMsgListQuery;
import com.linkwechat.domain.wecom.vo.customer.msg.WeGroupMsgListVo;
import com.linkwechat.fegin.QwAuthClient;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.fegin.QwUserClient;
import com.linkwechat.mapper.WeGroupMessageTemplateMapper;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 群发消息模板(WeGroupMessageTemplate)
 *
 * @author danmo
 * @since 2022-04-06 22:29:06
 */
@Slf4j
@Service
public class WeGroupMessageTemplateServiceImpl extends ServiceImpl<WeGroupMessageTemplateMapper, WeGroupMessageTemplate> implements IWeGroupMessageTemplateService {


    @Autowired
    private IWeGroupMessageListService weGroupMessageListService;

    @Autowired
    private IWeGroupMessageAttachmentsService attachmentsService;

    @Autowired
    private IWeGroupMessageTaskService messageTaskService;

    @Autowired
    private IWeGroupMessageSendResultService messageSendResultService;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private QwCustomerClient qwCustomerClient;

    @Autowired
    private QwSysUserClient qwSysUserClient;






    @Override
    @DataScope(type = "2", value = @DataColumn(alias = "we_group_message_template", name = "create_by_id", userid = "user_id"))
    public List<WeGroupMessageTemplate> queryList(WeGroupMessageTemplate weGroupMessageTemplate) {
        LambdaQueryWrapper<WeGroupMessageTemplate> lqw = Wrappers.lambdaQuery();
        if (weGroupMessageTemplate.getChatType() != null) {
            lqw.eq(WeGroupMessageTemplate::getChatType, weGroupMessageTemplate.getChatType());
        }
        if (StringUtils.isNotBlank(weGroupMessageTemplate.getContent())) {
            lqw.like(WeGroupMessageTemplate::getContent, weGroupMessageTemplate.getContent());
        }
        if (weGroupMessageTemplate.getBeginTime() != null) {
            lqw.ge(WeGroupMessageTemplate::getSendTime, weGroupMessageTemplate.getSendTime());
        }
        if (weGroupMessageTemplate.getEndTime() != null) {
            lqw.le(WeGroupMessageTemplate::getSendTime, weGroupMessageTemplate.getSendTime());
        }
        if (weGroupMessageTemplate.getIsTask() != null) {
            lqw.eq(WeGroupMessageTemplate::getIsTask, weGroupMessageTemplate.getIsTask());
        }
        if (weGroupMessageTemplate.getStatus() != null) {
            lqw.eq(WeGroupMessageTemplate::getStatus, weGroupMessageTemplate.getStatus());
        }
        if (CollectionUtil.isNotEmpty(weGroupMessageTemplate.getParams()) && ObjectUtil.isNotNull(weGroupMessageTemplate.getParams().get("dataScope"))) {
            String dataScope = String.valueOf(weGroupMessageTemplate.getParams().get("dataScope"));
            if (StringUtils.isNotEmpty(dataScope)) {
                lqw.apply(dataScope);
            }
        }
        return this.list(lqw);
    }

    @Override
    public WeGroupMessageDetailVo getGroupMsgTemplateDetail(Long id) {
        WeGroupMessageTemplate weGroupMessageTemplate = getById(id);
        Integer msgTemplate = weGroupMessageTemplate.getChatType();

        WeGroupMessageDetailVo detailVo = new WeGroupMessageDetailVo();
        detailVo.setStatus(weGroupMessageTemplate.getStatus());
        detailVo.setChatType(msgTemplate);
        detailVo.setRefreshTime(weGroupMessageTemplate.getRefreshTime());
        detailVo.setSendTime(weGroupMessageTemplate.getSendTime());
        detailVo.setContent(weGroupMessageTemplate.getContent());
        detailVo.setIsAll(weGroupMessageTemplate.isAllSend());
        detailVo.setWeCustomersOrGroupQuery(weGroupMessageTemplate.getWeCustomersOrGroupQuery());
        List<WeGroupMessageAttachments> attachmentsList = attachmentsService.lambdaQuery().eq(WeGroupMessageAttachments::getMsgTemplateId, id).list();
        detailVo.setAttachments(attachmentsList);
        List<WeGroupMessageTask> taskList = messageTaskService.lambdaQuery().eq(WeGroupMessageTask::getMsgTemplateId, id).list();
        if (CollectionUtil.isNotEmpty(taskList)) {
            //待发送人员列表
            Long toBeSent = taskList.stream().filter(item -> ObjectUtil.equal(0, item.getStatus())).count();
            detailVo.setToBeSendNum(toBeSent.intValue());
            //已发送人员列表
            Long alreadySent = taskList.stream().filter(item -> ObjectUtil.equal(2, item.getStatus())).count();
            detailVo.setAlreadySendNum(alreadySent.intValue());
        }
        List<WeGroupMessageSendResult> resultList = messageSendResultService.lambdaQuery().eq(WeGroupMessageSendResult::getMsgTemplateId, id).list();
        if (CollectionUtil.isNotEmpty(resultList)) {
            //未发送客户数
            Long toBeCustomerNum = resultList.stream().filter(item -> ObjectUtil.equal(0, item.getStatus())).count();
            detailVo.setToBeSendCustomerNum(toBeCustomerNum.intValue());
            //已发送客户数
            Long alreadyCustomerNum = resultList.stream().filter(item -> ObjectUtil.equal(1, item.getStatus())).count();
            detailVo.setAlreadySendCustomerNum(alreadyCustomerNum.intValue());
        }
        return detailVo;
    }


    //@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void addGroupMsgTemplate(WeAddGroupMessageQuery query) {
        log.info("addGroupMsgTemplate 入参：query:{}", JSONObject.toJSONString(query));
        List<WeAddGroupMessageQuery.SenderInfo> senderList = query.getSenderList();
        query.setAllSend(query.getIsAll());
        if(query.getChatType().equals(new Integer(1))){ //发送客户
            query.setWeCustomersOrGroupQuery(
                    WeGroupMessageTemplate.WeCustomersOrGroupQuery.builder()
                            .weCustomersQuery(query.getWeCustomersQuery())
                            .build()
            );
        }else if(query.getChatType().equals(new Integer(2))){ //发送客群


            if(CollectionUtil.isNotEmpty(senderList)){
                AjaxResult<List<SysUserVo>> sysUserVos = qwSysUserClient.getUserListByWeUserIds(SysUserQuery.builder()
                        .weUserIds(senderList.stream().map(WeAddGroupMessageQuery.SenderInfo::getUserId).collect(Collectors.toList()))
                        .build());

                query.setWeCustomersOrGroupQuery(
                        WeGroupMessageTemplate.WeCustomersOrGroupQuery.builder()
                                .weGroupQuery(
                                        WeGroupMessageTemplate.WeGroupQuery.builder()
                                                .owners(senderList.stream().map(WeAddGroupMessageQuery.SenderInfo::getUserId).collect(Collectors.joining(",")))
                                                .ownerNames(CollectionUtil.isNotEmpty(sysUserVos.getData())?sysUserVos.getData().stream().map(SysUserVo::getUserName).collect(Collectors.joining(",")):null)
                                                .build()
                                )
                                .build()
                );
            }


        }

        checkSenderList(query, senderList);
        WeGroupMessageTemplate weGroupMessageTemplate = new WeGroupMessageTemplate();
        BeanUtil.copyProperties(query, weGroupMessageTemplate);
        weGroupMessageTemplate.setId(null);
        if (query.getSendTime() == null) {
            weGroupMessageTemplate.setSendTime(new Date());
        }
        if (StringUtils.isEmpty(query.getContent()) && CollectionUtil.isNotEmpty(query.getAttachmentsList())) {
            String msgContent = query.getAttachmentsList().stream().map(item -> MessageType.messageTypeOf(item.getMsgType()).getName()).collect(Collectors.joining(","));
            weGroupMessageTemplate.setContent(msgContent);
        }
        if (save(weGroupMessageTemplate)) {
            query.setId(weGroupMessageTemplate.getId());
            query.setCurrentUserInfo(SecurityUtils.getLoginUser());
            //保存附件
            List<WeGroupMessageAttachments> attachmentsList = Optional.ofNullable(query.getAttachmentsList()).orElseGet(ArrayList::new).stream().map(attachment -> {
                WeGroupMessageAttachments attachments = new WeGroupMessageAttachments();
                BeanUtil.copyProperties(attachment, attachments);
                attachments.setMsgTemplateId(weGroupMessageTemplate.getId());
                return attachments;
            }).collect(Collectors.toList());

            attachmentsService.saveBatch(attachmentsList);


            List<WeGroupMessageList> weGroupMessageLists = new ArrayList<>();
            List<WeGroupMessageTask> messageTaskList = new ArrayList<>();
            List<WeGroupMessageSendResult> sendResultList = new ArrayList<>();
            for (WeAddGroupMessageQuery.SenderInfo senderInfo : senderList) {
                WeGroupMessageList weGroupMessageList = new WeGroupMessageList();
                weGroupMessageList.setMsgTemplateId(weGroupMessageTemplate.getId());
                if (query.getChatType() == 1) {
                    weGroupMessageList.setChatType("single");
                } else {
                    weGroupMessageList.setChatType("group");
                }
                weGroupMessageList.setUserId(senderInfo.getUserId());
                weGroupMessageLists.add(weGroupMessageList);

                WeGroupMessageTask messageTask = new WeGroupMessageTask();
                messageTask.setMsgTemplateId(weGroupMessageTemplate.getId());
                messageTask.setUserId(senderInfo.getUserId());
                messageTask.setWeCustomersQuery(query.getWeCustomersQuery());
                messageTaskList.add(messageTask);

                if (CollectionUtil.isNotEmpty(senderInfo.getCustomerList())) {
                    List<WeGroupMessageSendResult> messageSendResults = senderInfo.getCustomerList().stream().map(eid -> {
                        WeGroupMessageSendResult messageSendResult = new WeGroupMessageSendResult();
                        messageSendResult.setMsgTemplateId(weGroupMessageTemplate.getId());
                        messageSendResult.setUserId(senderInfo.getUserId());
                        messageSendResult.setExternalUserid(eid);
                        return messageSendResult;
                    }).collect(Collectors.toList());
                    sendResultList.addAll(messageSendResults);
                }
                if (CollectionUtil.isNotEmpty(senderInfo.getChatList())) {
                    List<WeGroupMessageSendResult> messageSendResults = senderInfo.getChatList().stream().map(chatId -> {
                        WeGroupMessageSendResult messageSendResult = new WeGroupMessageSendResult();
                        messageSendResult.setMsgTemplateId(weGroupMessageTemplate.getId());
                        messageSendResult.setUserId(senderInfo.getUserId());
                        messageSendResult.setChatId(chatId);
                        return messageSendResult;
                    }).collect(Collectors.toList());
                    sendResultList.addAll(messageSendResults);
                }
            }

            //保存发送任务
            weGroupMessageListService.saveBatch(weGroupMessageLists);
            //保存成员发送任务
            messageTaskService.saveBatch(messageTaskList);
            //保存发送客户或者客户群
            messageSendResultService.saveBatch(sendResultList);

            if (ObjectUtil.equal(0, query.getIsTask()) && query.getSendTime() == null) {
                //todo 立即发送
                rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeGroupMsgRk(), JSONObject.toJSONString(query));
            } else {
                //todo 延时发送
                long diffTime = DateUtils.diffTime(query.getSendTime(), new Date());
                rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeDelayGroupMsgRk(), JSONObject.toJSONString(query), message -> {
                    //注意这里时间可使用long类型,毫秒单位，设置header
                    message.getMessageProperties().setHeader("x-delay", diffTime);
                    return message;
                });
            }

        }
    }

    private void checkSenderList(WeAddGroupMessageQuery query, List<WeAddGroupMessageQuery.SenderInfo> senderList) {
        if (query.getIsAll() && CollectionUtil.isEmpty(senderList)) {
            if (query.getChatType() == 1) {
                List<WeAddGroupMessageQuery.SenderInfo> limitSenderInfoWeCustomerList = weCustomerService.findLimitSenderInfoWeCustomerList();

                if(CollectionUtil.isNotEmpty(limitSenderInfoWeCustomerList)){

                    senderList.addAll(limitSenderInfoWeCustomerList);


                } else {
                    throw new WeComException("暂无客户可发送");
                }
            } else {
                List<WeGroup> groupList = weGroupService.list(new LambdaQueryWrapper<WeGroup>().eq(WeGroup::getDelFlag, Constants.COMMON_STATE));
                if (CollectionUtil.isNotEmpty(groupList)) {
                    Map<String, List<String>> ownerToChatIdMap = groupList.stream().collect(Collectors.groupingBy(WeGroup::getOwner, Collectors.mapping(WeGroup::getChatId, Collectors.toList())));
                    ownerToChatIdMap.forEach((userId, chatIds) -> {
                        WeAddGroupMessageQuery.SenderInfo senderInfo = new WeAddGroupMessageQuery.SenderInfo();
                        senderInfo.setUserId(userId);
                        senderInfo.setChatList(chatIds);
                        senderList.add(senderInfo);
                    });
                } else {
                    throw new WeComException("暂无客户群可发送");
                }
            }
        } else if (!query.getIsAll() && CollectionUtil.isEmpty(senderList)) {
            throw new WeComException("无指定接收消息的成员及对应客户列表");
        } else if (!query.getIsAll() && CollectionUtil.isNotEmpty(senderList) && query.getChatType() == 2) {
            List<String> userIds = senderList.stream().map(WeAddGroupMessageQuery.SenderInfo::getUserId).collect(Collectors.toList());
            List<WeGroup> groupList = weGroupService.list(new LambdaQueryWrapper<WeGroup>().in(WeGroup::getOwner, userIds).eq(WeGroup::getDelFlag, 0));
            if (CollectionUtil.isNotEmpty(groupList)) {
                Map<String, List<String>> ownerToChatIdMap = groupList.stream().collect(Collectors.groupingBy(WeGroup::getOwner, Collectors.mapping(WeGroup::getChatId, Collectors.toList())));
                for (WeAddGroupMessageQuery.SenderInfo senderInfo : senderList) {
                    List<String> chatIds = ownerToChatIdMap.get(senderInfo.getUserId());
                    senderInfo.setChatList(chatIds);
                }
            }
        }
    }

    @Override
    public void cancelByIds(List<Long> asList) {
        WeGroupMessageTemplate template = new WeGroupMessageTemplate();
        template.setStatus(2);
        update(template, new LambdaQueryWrapper<WeGroupMessageTemplate>().in(WeGroupMessageTemplate::getId, asList));
    }

    @Async
    @Override
    public void syncGroupMsgSendResultByIds(List<Long> asList) {
        WeGroupMessageTemplate template = new WeGroupMessageTemplate();
        template.setRefreshTime(new Date());
        this.update(template, new LambdaQueryWrapper<WeGroupMessageTemplate>().in(WeGroupMessageTemplate::getId, asList));
        List<WeGroupMessageList> weGroupMessageLists = weGroupMessageListService.list(new LambdaQueryWrapper<WeGroupMessageList>()
                .in(WeGroupMessageList::getMsgTemplateId, asList)
                .eq(WeGroupMessageList::getDelFlag, 0));
        List<WeGroupMessageTask> taskList = new ArrayList<>();
        List<WeGroupMessageSendResult> sendResultlist = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(weGroupMessageLists)) {
            weGroupMessageLists.forEach(weGroupMessageList -> {
                WeGetGroupMsgListQuery weGetGroupMsgListQuery = new WeGetGroupMsgListQuery();
                weGetGroupMsgListQuery.setMsgid(weGroupMessageList.getMsgId());
                WeGroupMsgListVo groupMsgTask = qwCustomerClient.getGroupMsgTask(weGetGroupMsgListQuery).getData();
                Optional.ofNullable(groupMsgTask).map(WeGroupMsgListVo::getTaskList).orElseGet(ArrayList::new).forEach(msgTask -> {
                    WeGroupMessageTask messageTask = new WeGroupMessageTask();
                    messageTask.setMsgId(weGroupMessageList.getMsgId());
                    messageTask.setUserId(msgTask.getUserId());
                    if (msgTask.getSendTime() != null) {
                        messageTask.setSendTime(new Date(msgTask.getSendTime() * 1000));
                    }
                    messageTask.setMsgTemplateId(weGroupMessageList.getMsgTemplateId());
                    messageTask.setStatus(msgTask.getStatus());
                    taskList.add(messageTask);

                    weGetGroupMsgListQuery.setUserid(msgTask.getUserId());
                    WeGroupMsgListVo groupMsgSendResult = qwCustomerClient.getGroupMsgSendResult(weGetGroupMsgListQuery).getData();
                    Optional.ofNullable(groupMsgSendResult).map(WeGroupMsgListVo::getSendList).orElseGet(ArrayList::new).forEach(sendResult -> {
                        WeGroupMessageSendResult messageSendResult = new WeGroupMessageSendResult();
                        messageSendResult.setMsgId(weGroupMessageList.getMsgId());
                        messageSendResult.setUserId(msgTask.getUserId());
                        messageSendResult.setChatId(sendResult.getChatId());
                        messageSendResult.setExternalUserid(sendResult.getExternalUserId());
                        if (sendResult.getSendTime() != null) {
                            messageSendResult.setSendTime(new Date(sendResult.getSendTime() * 1000));
                        }
                        messageSendResult.setStatus(sendResult.getStatus());
                        messageSendResult.setMsgTemplateId(weGroupMessageList.getMsgTemplateId());
                        sendResultlist.add(messageSendResult);
                    });
                });

            });
        }
        messageTaskService.addOrUpdateBatchByCondition(taskList);
        messageSendResultService.addOrUpdateBatchByCondition(sendResultlist);
    }


    @Override
    public List<WeGroupMessageTask> groupMsgTaskList(WeGroupMessageTask task) {
        return messageTaskService.groupMsgTaskList(task);
    }

    @Override
    public List<WeGroupMessageSendResult> groupMsgSendResultList(WeGroupMessageSendResult sendResult) {
        return messageSendResultService.groupMsgSendResultList(sendResult);
    }


    @Async
    @Override
    public void groupMessageTaskHandler(WeAddGroupMessageQuery query) {

        if (query.getMsgSource().equals(new Integer(2))) {//sop
            //sop消息群发逻辑
            SpringUtils.getBean("sopGroupMsgService", AbstractGroupMsgSendTaskService.class).sendGroupMsg(query);

        } else if (query.getMsgSource().equals(new Integer(3))) {//直播
            //直播消息群发逻辑
            SpringUtils.getBean("liveGroupMsgService", AbstractGroupMsgSendTaskService.class).sendGroupMsg(query);

        } else if (query.getMsgSource().equals(new Integer(5))) {
            //短链推广群发逻辑
            SpringUtils.getBean("shortLinkPromotionGroupMsgService", AbstractGroupMsgSendTaskService.class).sendGroupMsg(query);

        } else if (query.getMsgSource().equals(new Integer(4))){
            //裂变群发逻辑
            SpringUtils.getBean("fissionGroupMsgService", AbstractGroupMsgSendTaskService.class).sendGroupMsg(query);


        }else if(query.getMsgSource().equals(new Integer(6))){
            //老客标签建群
            SpringUtils.getBean("wePresTagGroupMsgService", AbstractGroupMsgSendTaskService.class).sendGroupMsg(query);

        } else {
            //公共消息群发逻辑
            SpringUtils.getBean("commonGroupMsgService", AbstractGroupMsgSendTaskService.class).sendGroupMsg(query);
        }

    }

    @Override
    public WeAddGroupMessageQuery findGroupMessageDetail(Long id) {
        WeAddGroupMessageQuery groupMessageQuery=new WeAddGroupMessageQuery();
        WeGroupMessageTemplate weGroupMessageTemplate
                = this.getById(id);
        if(null != weGroupMessageTemplate){
            BeanUtil.copyProperties(weGroupMessageTemplate,groupMessageQuery);
            groupMessageQuery.setWeGroupMessageAttachmentsVo(
                    attachmentsService.list(new LambdaQueryWrapper<WeGroupMessageAttachments>()
                            .eq(WeGroupMessageAttachments::getMsgTemplateId,id))
            );
        }

        return groupMessageQuery;
    }

}
