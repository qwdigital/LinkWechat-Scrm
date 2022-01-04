package com.linkwechat.wecom.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgListDto;
import com.linkwechat.wecom.domain.query.WeAddGroupMessageQuery;
import com.linkwechat.wecom.domain.vo.WeGroupMessageDetailVo;
import com.linkwechat.wecom.domain.vo.WeGroupMessageListVo;
import com.linkwechat.wecom.domain.vo.WeGroupMessageTaskVo;
import com.linkwechat.wecom.mapper.WeGroupMessageTemplateMapper;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 群发消息模板Service业务层处理
 *
 * @author ruoyi
 * @date 2021-10-27
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
    private RedisCache redisCache;

    @Override
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
        return this.list(lqw);
    }

    @Override
    public WeGroupMessageDetailVo getGroupMsgTemplateDetail(Long id) {
        WeGroupMessageTemplate weGroupMessageTemplate = getById(id);
        Integer msgTemplate = weGroupMessageTemplate.getChatType();

        WeGroupMessageDetailVo detailVo = new WeGroupMessageDetailVo();
        detailVo.setChatType(msgTemplate);
        detailVo.setRefreshTime(weGroupMessageTemplate.getRefreshTime());
        detailVo.setSendTime(weGroupMessageTemplate.getSendTime());
        detailVo.setContent(weGroupMessageTemplate.getContent());
        List<WeGroupMessageAttachments> attachmentsList = attachmentsService.lambdaQuery().eq(WeGroupMessageAttachments::getMsgTemplateId, id).list();
        detailVo.setAttachments(attachmentsList);
        List<WeGroupMessageTask> taskList = messageTaskService.lambdaQuery().eq(WeGroupMessageTask::getMsgTemplateId, id).list();
        if(CollectionUtil.isNotEmpty(taskList)){
            //待发送人员列表
            Long toBeSent = taskList.stream().filter(item -> ObjectUtil.equal(0, item.getStatus())).count();
            detailVo.setToBeSendNum(toBeSent.intValue());
            //已发送人员列表
            Long alreadySent = taskList.stream().filter(item -> ObjectUtil.equal(2, item.getStatus())).count();
            detailVo.setAlreadySendNum(alreadySent.intValue());
        }
        List<WeGroupMessageSendResult> resultList = messageSendResultService.lambdaQuery().eq(WeGroupMessageSendResult::getMsgTemplateId, id).list();
        if(CollectionUtil.isNotEmpty(resultList)){
            //未发送客户数
            Long toBeCustomerNum = resultList.stream().filter(item -> ObjectUtil.equal(0, item.getStatus())).count();
            detailVo.setToBeSendCustomerNum(toBeCustomerNum.intValue());
            //已发送客户数
            Long alreadyCustomerNum = resultList.stream().filter(item -> ObjectUtil.equal(1, item.getStatus())).count();
            detailVo.setAlreadySendCustomerNum(alreadyCustomerNum.intValue());
        }
        /*List<WeGroupMessageListVo> groupMsgDetail = weGroupMessageListService.getGroupMsgDetail(id);
        if (CollectionUtil.isNotEmpty(groupMsgDetail)) {
            detailVo.setAttachments(groupMsgDetail.get(0).getAttachments());
            //待发送人员列表
            Long toBeSent = groupMsgDetail.stream().map(WeGroupMessageListVo::getSenders)
                    .flatMap(Collection::stream).filter(item -> ObjectUtil.equal(0, item.getStatus())).count();
            detailVo.setToBeSendNum(toBeSent.intValue());
            //已发送人员列表
            Long alreadySent = groupMsgDetail.stream().map(WeGroupMessageListVo::getSenders)
                    .flatMap(Collection::stream).filter(item -> ObjectUtil.equal(2, item.getStatus())).count();
            detailVo.setAlreadySendNum(alreadySent.intValue());
            //未发送客户数
            Long toBeCustomerNum = groupMsgDetail.stream().map(WeGroupMessageListVo::getExtralInfos).flatMap(Collection::stream)
                    .filter(item -> ObjectUtil.equal(0, item.getStatus())).count();
            detailVo.setToBeSendCustomerNum(toBeCustomerNum.intValue());
            //已发送客户数
            Long alreadyCustomerNum = groupMsgDetail.stream().map(WeGroupMessageListVo::getExtralInfos).flatMap(Collection::stream)
                    .filter(item -> ObjectUtil.equal(1, item.getStatus())).count();
            detailVo.setAlreadySendCustomerNum(alreadyCustomerNum.intValue());
            //未发送查询每个人员对应客户信息
            if (CollectionUtil.isNotEmpty(toBeSent)) {
                List<WeGroupMessageTaskVo> toBeSentList = toBeSent.stream().map(userInfo -> {
                    WeGroupMessageTaskVo weGroupMessageTaskVo = new WeGroupMessageTaskVo();
                    weGroupMessageTaskVo.setUserId(userInfo.getUserId());
                    weGroupMessageTaskVo.setUserName(userInfo.getUserName());
                    if (msgTemplate == 1) {
                        List<String> customerList = groupMsgDetail.stream().map(WeGroupMessageListVo::getExtralInfos)
                                .flatMap(Collection::stream).filter(item -> ObjectUtil.equal(userInfo.getMsgId(), item.getMsgId())
                                        && ObjectUtil.equal(userInfo.getUserId(), item.getUserId())
                                        && ObjectUtil.equal(0, item.getStatus())).map(WeGroupMessageSendResult::getCustomerName).collect(Collectors.toList());
                        weGroupMessageTaskVo.setCustomerList(customerList);
                    } else {
                        List<String> chatNameList = groupMsgDetail.stream().map(WeGroupMessageListVo::getExtralInfos)
                                .flatMap(Collection::stream).filter(item -> ObjectUtil.equal(userInfo.getMsgId(), item.getMsgId())
                                        && ObjectUtil.equal(userInfo.getUserId(), item.getUserId())
                                        && !ObjectUtil.equal(2, item.getStatus())).map(WeGroupMessageSendResult::getChatName).collect(Collectors.toList());
                        weGroupMessageTaskVo.setGroupList(chatNameList);
                    }
                    return weGroupMessageTaskVo;
                }).collect(Collectors.toList());
                detailVo.setToBeSendList(toBeSentList);
                detailVo.setToBeSendNum(toBeSentList.size());
            }*/
            //已发送查询每个人员对应客户信息
            /*if (CollectionUtil.isNotEmpty(alreadySent)) {
                List<WeGroupMessageTaskVo> alreadySentList = alreadySent.stream().map(userInfo -> {
                    WeGroupMessageTaskVo weGroupMessageTaskVo = new WeGroupMessageTaskVo();
                    weGroupMessageTaskVo.setUserId(userInfo.getUserId());
                    weGroupMessageTaskVo.setUserName(userInfo.getUserName());
                    weGroupMessageTaskVo.setSendTime(userInfo.getSendTime());
                    if (msgTemplate == 1) {
                        List<String> customerList = groupMsgDetail.stream().map(WeGroupMessageListVo::getExtralInfos)
                                .flatMap(Collection::stream).filter(item -> ObjectUtil.equal(userInfo.getMsgId(), item.getMsgId())
                                        && ObjectUtil.equal(userInfo.getUserId(), item.getUserId())
                                        && ObjectUtil.equal(2, item.getStatus())).map(WeGroupMessageSendResult::getCustomerName).collect(Collectors.toList());
                        weGroupMessageTaskVo.setCustomerList(customerList);
                    } else {
                        List<String> chatNameList = groupMsgDetail.stream().map(WeGroupMessageListVo::getExtralInfos)
                                .flatMap(Collection::stream).filter(item -> ObjectUtil.equal(userInfo.getMsgId(), item.getMsgId())
                                        && ObjectUtil.equal(userInfo.getUserId(), item.getUserId())
                                        && ObjectUtil.equal(2, item.getStatus())).map(WeGroupMessageSendResult::getChatName).collect(Collectors.toList());
                        weGroupMessageTaskVo.setGroupList(chatNameList);
                    }
                    return weGroupMessageTaskVo;
                }).collect(Collectors.toList());
                detailVo.setAlreadySendList(alreadySentList);
                detailVo.setAlreadySendNum(alreadySentList.size());
            }
        }*/
        return detailVo;
    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void addGroupMsgTemplate(WeAddGroupMessageQuery query) throws Exception{
        log.info("addGroupMsgTemplate 入参：query:{}",JSONObject.toJSONString(query));
        List<WeAddGroupMessageQuery.SenderInfo> senderList = query.getSenderList();

        checkSenderList(query, senderList);
        WeGroupMessageTemplate weGroupMessageTemplate = new WeGroupMessageTemplate();
        BeanUtil.copyProperties(query, weGroupMessageTemplate);
        if(query.getSendTime() == null){
            weGroupMessageTemplate.setSendTime(new Date());
        }
        if(StringUtils.isEmpty(query.getContent()) && CollectionUtil.isNotEmpty(query.getAttachmentsList())){
            String msgContent = query.getAttachmentsList().stream().map(item -> MessageType.messageTypeOf(item.getMsgType()).getName()).collect(Collectors.joining(","));
            weGroupMessageTemplate.setContent(msgContent);
        }
        if (save(weGroupMessageTemplate)) {
            query.setId(weGroupMessageTemplate.getId());
            //保存附件
            List<WeGroupMessageAttachments> attachmentsList = Optional.ofNullable(query.getAttachmentsList()).orElseGet(ArrayList::new).stream().map(attachment -> {
                WeGroupMessageAttachments attachments = new WeGroupMessageAttachments();
                BeanUtil.copyProperties(attachment, attachments);
                attachments.setMsgTemplateId(weGroupMessageTemplate.getId());
                return attachments;
            }).collect(Collectors.toList());

            /*if (StringUtils.isNotEmpty(query.getContent())) {
                WeGroupMessageAttachments attachments = new WeGroupMessageAttachments();
                attachments.setMsgTemplateId(weGroupMessageTemplate.getId());
                attachments.setContent(query.getContent());
                attachments.setMsgType(MessageType.TEXT.getMessageType());
                attachmentsList.add(attachments);
            }*/
            attachmentsService.saveBatch(attachmentsList);


            List<WeGroupMessageList> weGroupMessageLists = new ArrayList<>();
            List<WeGroupMessageTask> messageTaskList = new ArrayList<>();
            List<WeGroupMessageSendResult> sendResultList = new ArrayList<>();
            for (WeAddGroupMessageQuery.SenderInfo senderInfo :senderList ) {
                WeGroupMessageList weGroupMessageList = new WeGroupMessageList();
                weGroupMessageList.setMsgTemplateId(weGroupMessageTemplate.getId());
                if(query.getChatType() == 1){
                    weGroupMessageList.setChatType("single");
                }else {
                    weGroupMessageList.setChatType("group");
                }
                weGroupMessageList.setUserId(senderInfo.getUserId());
                weGroupMessageLists.add(weGroupMessageList);

                WeGroupMessageTask messageTask = new WeGroupMessageTask();
                messageTask.setMsgTemplateId(weGroupMessageTemplate.getId());
                messageTask.setUserId(senderInfo.getUserId());
                messageTaskList.add(messageTask);

                List<WeGroupMessageSendResult> messageSendResults = Optional.ofNullable(senderInfo.getCustomerList())
                        .orElseGet(ArrayList::new).stream().map(eid -> {
                            WeGroupMessageSendResult messageSendResult = new WeGroupMessageSendResult();
                            messageSendResult.setMsgTemplateId(weGroupMessageTemplate.getId());
                            messageSendResult.setUserId(senderInfo.getUserId());
                            messageSendResult.setExternalUserid(eid);
                            return messageSendResult;
                        }).collect(Collectors.toList());
                sendResultList.addAll(messageSendResults);
            }

            //保存发送任务
            weGroupMessageListService.saveBatch(weGroupMessageLists);
            //保存成员发送任务
            messageTaskService.saveBatch(messageTaskList);
            //保存发送客户或者客户群
            messageSendResultService.saveBatch(sendResultList);

            if (ObjectUtil.equal(0, query.getIsTask()) && query.getSendTime() == null) {
                redisCache.setCacheZSet(WeConstans.WEGROUPMSGTIMEDTASK_KEY, JSONObject.toJSONString(query), System.currentTimeMillis());
            } else {
                redisCache.setCacheZSet(WeConstans.WEGROUPMSGTIMEDTASK_KEY, JSONObject.toJSONString(query), query.getSendTime().getTime());
            }

        }
    }

    private void checkSenderList(WeAddGroupMessageQuery query, List<WeAddGroupMessageQuery.SenderInfo> senderList) {
        if(query.getIsAll() && CollectionUtil.isEmpty(senderList)){
            if(query.getChatType() == 1){
                List<WeCustomer> customerList = weCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                        .select(WeCustomer::getExternalUserid,WeCustomer::getFirstUserId)
                        .eq(WeCustomer::getDelFlag, 0).groupBy(WeCustomer::getExternalUserid,WeCustomer::getFirstUserId));
                if(CollectionUtil.isNotEmpty(customerList)){
                    Map<String, List<WeCustomer>> customerMap = customerList.stream().collect(Collectors.groupingBy(WeCustomer::getFirstUserId));
                    customerMap.forEach((userId, customers) ->{
                        List<String> eids = customers.stream().map(WeCustomer::getExternalUserid).collect(Collectors.toList());
                        WeAddGroupMessageQuery.SenderInfo senderInfo = new WeAddGroupMessageQuery.SenderInfo();
                        senderInfo.setCustomerList(eids);
                        senderInfo.setUserId(userId);
                        senderList.add(senderInfo);
                    });
                }else {
                    throw new WeComException("暂无客户可发送");
                }
            }else {
                List<WeGroup> groupList = weGroupService.list(new LambdaQueryWrapper<WeGroup>()
                        .select(WeGroup::getAdminUserId).eq(WeGroup::getDelFlag, 0).groupBy(WeGroup::getAdminUserId));
                if(CollectionUtil.isNotEmpty(groupList)){
                    groupList.forEach(group ->{
                        WeAddGroupMessageQuery.SenderInfo senderInfo = new WeAddGroupMessageQuery.SenderInfo();
                        senderInfo.setUserId(group.getAdminUserId());
                        senderList.add(senderInfo);
                    });

                }else {
                    throw new WeComException("暂无客户群可发送");
                }
            }
        }else if(!query.getIsAll() && CollectionUtil.isEmpty(senderList)){
            throw new WeComException("无指定接收消息的成员及对应客户列表");
        }
    }

    @Override
    public void cancelByIds(List<Long> asList) {
        List<WeGroupMessageTemplate> weGroupMessageTemplates = listByIds(asList);
        if(CollectionUtil.isNotEmpty(weGroupMessageTemplates)){
            weGroupMessageTemplates.forEach(weGroupMessageTemplate -> {
                weGroupMessageTemplate.setStatus(2);
                long time = weGroupMessageTemplate.getSendTime().getTime();
                Set<ZSetOperations.TypedTuple<String>> typedTuples = redisCache.sortRangeWithScoresCacheZSet(WeConstans.WEGROUPMSGTIMEDTASK_KEY, time, time);
                if(CollectionUtil.isNotEmpty(typedTuples)){
                    typedTuples.forEach(typedTuple ->{
                        redisCache.removeCacheZSet(WeConstans.WEGROUPMSGTIMEDTASK_KEY,typedTuple.getValue());
                    });
                }
            });
            updateBatchById(weGroupMessageTemplates);
        }
    }

    @Async
    @Override
    public void syncGroupMsgSendResultByIds(List<Long> asList) {
        WeGroupMessageTemplate template = new WeGroupMessageTemplate();
        template.setRefreshTime(new Date());
        this.update(template,new LambdaQueryWrapper<WeGroupMessageTemplate>().in(WeGroupMessageTemplate::getId,asList));
        List<WeGroupMessageList> weGroupMessageLists = weGroupMessageListService.list(new LambdaQueryWrapper<WeGroupMessageList>()
                .in(WeGroupMessageList::getMsgTemplateId, asList));
        List<WeGroupMessageTask> taskList = new ArrayList<>();
        List<WeGroupMessageSendResult> sendResultlist = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(weGroupMessageLists)){
            weGroupMessageLists.forEach(weGroupMessageList -> {
                WeGroupMsgListDto groupMsgTask = messageTaskService.getGroupMsgTask(weGroupMessageList.getMsgId(), null);
                Optional.ofNullable(groupMsgTask).map(WeGroupMsgListDto::getTaskList).orElseGet(ArrayList::new).forEach(msgTask -> {
                    WeGroupMessageTask messageTask = new WeGroupMessageTask();
                    messageTask.setMsgId(weGroupMessageList.getMsgId());
                    messageTask.setUserId(msgTask.getUserId());
                    if(msgTask.getSendTime() != null){
                        messageTask.setSendTime(new Date(msgTask.getSendTime() * 1000));
                    }
                    messageTask.setMsgTemplateId(weGroupMessageList.getMsgTemplateId());
                    messageTask.setStatus(msgTask.getStatus());
                    taskList.add(messageTask);

                    WeGroupMsgListDto groupMsgSendResult = messageSendResultService.getGroupMsgSendResult(weGroupMessageList.getMsgId(), msgTask.getUserId(), null);
                    Optional.ofNullable(groupMsgSendResult).map(WeGroupMsgListDto::getSendList).orElseGet(ArrayList::new).forEach(sendResult -> {
                        WeGroupMessageSendResult messageSendResult = new WeGroupMessageSendResult();
                        messageSendResult.setMsgId(weGroupMessageList.getMsgId());
                        messageSendResult.setUserId(msgTask.getUserId());
                        messageSendResult.setChatId(sendResult.getChatId());
                        messageSendResult.setExternalUserid(sendResult.getExternalUserId());
                        if(sendResult.getSendTime() != null){
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
}
