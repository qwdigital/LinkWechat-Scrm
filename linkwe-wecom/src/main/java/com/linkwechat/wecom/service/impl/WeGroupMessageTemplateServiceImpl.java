package com.linkwechat.wecom.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgListDto;
import com.linkwechat.wecom.domain.query.WeAddGroupMessageQuery;
import com.linkwechat.wecom.domain.vo.WeGroupMessageDetailVo;
import com.linkwechat.wecom.domain.vo.WeGroupMessageListVo;
import com.linkwechat.wecom.domain.vo.WeGroupMessageTaskVo;
import com.linkwechat.wecom.mapper.WeGroupMessageTemplateMapper;
import com.linkwechat.wecom.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 群发消息模板Service业务层处理
 *
 * @author ruoyi
 * @date 2021-10-27
 */
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
        detailVo.setSendTime(weGroupMessageTemplate.getSendTime());
        detailVo.setContent(weGroupMessageTemplate.getContent());
        List<WeGroupMessageListVo> groupMsgDetail = weGroupMessageListService.getGroupMsgDetail(id);
        if (groupMsgDetail != null) {
            detailVo.setAttachments(groupMsgDetail.get(0).getAttachments());

            //待发送人员列表
            List<WeGroupMessageTask> toBeSent = groupMsgDetail.stream().map(WeGroupMessageListVo::getSenders)
                    .flatMap(Collection::stream).filter(item -> ObjectUtil.equal(0, item.getStatus())).collect(Collectors.toList());

            //已发送人员列表
            List<WeGroupMessageTask> alreadySent = groupMsgDetail.stream().map(WeGroupMessageListVo::getSenders)
                    .flatMap(Collection::stream).filter(item -> ObjectUtil.equal(2, item.getStatus())).collect(Collectors.toList());

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
                detailVo.setToBeSentList(toBeSentList);
            }
            //已发送查询每个人员对应客户信息
            if (CollectionUtil.isNotEmpty(alreadySent)) {
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
                detailVo.setAlreadySentList(alreadySentList);
            }
        }
        return detailVo;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addGroupMsgTemplate(WeAddGroupMessageQuery query) {
        List<WeAddGroupMessageQuery.SenderInfo> senderList = query.getSenderList();
        WeGroupMessageTemplate weGroupMessageTemplate = new WeGroupMessageTemplate();
        BeanUtil.copyProperties(query, weGroupMessageTemplate);
        if(query.getSendTime() == null){
            weGroupMessageTemplate.setSendTime(new Date());
        }
        try {
            if (save(weGroupMessageTemplate)) {
                query.setId(weGroupMessageTemplate.getId());
                //保存附件
                List<WeGroupMessageAttachments> attachmentsList = Optional.ofNullable(query.getAttachmentsList()).orElseGet(ArrayList::new).stream().map(attachment -> {
                    WeGroupMessageAttachments attachments = new WeGroupMessageAttachments();
                    BeanUtil.copyProperties(attachment, attachments);
                    attachments.setMsgTemplateId(weGroupMessageTemplate.getId());
                    return attachments;
                }).collect(Collectors.toList());

                if (StringUtils.isNotEmpty(query.getContent())) {
                    WeGroupMessageAttachments attachments = new WeGroupMessageAttachments();
                    attachments.setMsgTemplateId(weGroupMessageTemplate.getId());
                    attachments.setContent(query.getContent());
                    attachments.setMsgType(MessageType.TEXT.getMessageType());
                    attachmentsList.add(attachments);
                }
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

                    List<WeGroupMessageSendResult> messageSendResults = senderInfo.getCustomerList().stream().map(eid -> {
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
        } catch (Exception e) {
            e.printStackTrace();
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

}