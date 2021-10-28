package com.linkwechat.quartz.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerMessagePushClient;
import com.linkwechat.wecom.domain.WeGroupMessageAttachments;
import com.linkwechat.wecom.domain.WeGroupMessageList;
import com.linkwechat.wecom.domain.WeGroupMessageSendResult;
import com.linkwechat.wecom.domain.WeGroupMessageTask;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.dto.message.SendMessageResultDto;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgListDto;
import com.linkwechat.wecom.domain.query.WeAddGroupMessageQuery;
import com.linkwechat.wecom.domain.query.WeAddMsgTemplateQuery;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author danmo
 * @description 消息群发任务列表拉取
 * @date 2021/10/15 22:59
 **/
@Slf4j
@Component("pullGroupMessageTask")
public class PullGroupMessageTask {

    @Autowired
    private IWeGroupMessageListService groupMessageListService;

    @Autowired
    private IWeGroupMessageAttachmentsService attachmentsService;

    @Autowired
    private IWeGroupMessageTaskService groupMessageTaskService;

    @Autowired
    private IWeGroupMessageSendResultService sendResultService;

    @Autowired
    private IWeMaterialService weMaterialService;

    @Autowired
    private WeCustomerMessagePushClient customerMessagePushClient;

    @Autowired
    private RedisCache redisCache;

    /**
     * 同步开始时间
     */
    private Long startTime;

    /**
     * 同步结束时间
     */
    private Long endTime;

    /**
     * 定时拉取群发消息
     *
     * @param type 1:全量同步 2:增量同步 3：全量同步个人消息 4:全量同步群组消息 5: 增量同步个人消息 6:增量同步群组消息
     */
    public void pullGroupMessage(String type) {
        if (ObjectUtil.equal("1", type)) {
            startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -30)).getTime() / 1000;
            endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
        } else if (ObjectUtil.equal("2", type)) {
            startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
            endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
        } else if (ObjectUtil.equal("3", type)) {
            startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -30)).getTime() / 1000;
            endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
            pullGroupMsgList();
            return;
        } else if (ObjectUtil.equal("4", type)) {
            startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -30)).getTime() / 1000;
            endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
            pullGroupMsgList();
            return;
        } else if (ObjectUtil.equal("5", type)) {
            startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
            endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
            pullSingleMsgList();
            return;
        } else if (ObjectUtil.equal("6", type)) {
            startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
            endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
            pullGroupMsgList();
            return;
        }
        //ThreadUtil.execAsync(this::pullSingleMsgList);
        pullGroupMsgList();
        pullSingleMsgList();
        //ThreadUtil.execAsync(this::pullGroupMsgList);
    }

    //拉取发送给客户的群发消息
    private void pullSingleMsgList() {
        WeGroupMsgListDto singleMsgList = groupMessageListService.getGroupMsgList("single", startTime, endTime, null);
        saveGroupMsg(singleMsgList,"single");
        pullGroupMsgSendResult(singleMsgList);

    }

    //拉取发送给客户群的群发消息
    private void pullGroupMsgList() {
        //拉取发送给客户的群发消息
        WeGroupMsgListDto groupMsgList = groupMessageListService.getGroupMsgList("group", startTime, endTime, null);
        saveGroupMsg(groupMsgList,"group");
        pullGroupMsgSendResult(groupMsgList);
    }

    /**
     * 保存消息入库
     * @param groupMsgList
     * @param chatType
     */
    private void saveGroupMsg(WeGroupMsgListDto groupMsgList,String chatType) {
        List<WeGroupMessageAttachments> attachmentList = new ArrayList<>();
        List<WeGroupMessageList> saveGroupMessageList = new ArrayList<>();
        Optional.ofNullable(groupMsgList).map(WeGroupMsgListDto::getGroupMsgList)
                .orElseGet(ArrayList::new).forEach(weGroupMsg -> {
            WeGroupMessageList weGroupMessageList = new WeGroupMessageList();
            weGroupMessageList.setMsgId(weGroupMsg.getMsgId());
            weGroupMessageList.setChatType(chatType);
            weGroupMessageList.setCreateType(weGroupMsg.getCreateType());
            weGroupMessageList.setSendTime(new Date(weGroupMsg.getCreateTime() * 1000));
            weGroupMessageList.setUserId(weGroupMsg.getCreator());
            WeGroupMessageList weGroupMessage = groupMessageListService.getOne(new LambdaQueryWrapper<WeGroupMessageList>()
                    .eq(WeGroupMessageList::getMsgId, weGroupMsg.getMsgId()).last("limit 1"));
            if (weGroupMessage != null) {
                weGroupMessageList.setId(weGroupMessage.getId());
                saveGroupMessageList.add(weGroupMessageList);
            } else {
                saveGroupMessageList.add(weGroupMessageList);
                String content = Optional.ofNullable(weGroupMsg.getText()).map(WeAddMsgTemplateQuery.Text::getContent).orElse("");
                if (StringUtils.isNotEmpty(content)) {
                    WeGroupMessageAttachments attachment = new WeGroupMessageAttachments();
                    attachment.setMsgId(weGroupMsg.getMsgId());
                    attachment.setMsgType("text");
                    attachment.setContent(content);
                    attachmentList.add(attachment);
                }
                List<JSONObject> attachments = weGroupMsg.getAttachments();
                Optional.ofNullable(attachments).orElseGet(ArrayList::new).forEach(attachment -> {
                    String msgtype = attachment.getString("msgtype");
                    JSONObject msgObject = attachment.getJSONObject(msgtype);
                    WeGroupMessageAttachments messageAttachment = new WeGroupMessageAttachments();
                    messageAttachment.setMsgId(weGroupMsg.getMsgId());
                    messageAttachment.setMsgType(msgtype);
                    String mediaId = msgObject.getString("media_id")==null ?msgObject.getString("pic_media_id")
                            :msgObject.getString("media_id");
                    WeMediaDto weMediaDto = null;
                    if(StringUtils.isNotEmpty(mediaId)){
                        try {
                            weMediaDto = weMaterialService.getMediaToResponse(mediaId);
                        } catch (Exception e) {
                            log.info("获取素材信息失败.........."+e.getMessage());
                        }
                    }
                    MessageType messageType = MessageType.messageTypeOf(msgtype);
                    switch (messageType) {
                        case IMAGE:
                            //messageAttachment.setMediaId(msgObject.getString("media_id"));
                            messageAttachment.setMediaId(mediaId);
                            messageAttachment.setPicUrl(msgObject.getString("pic_url"));
                            if(weMediaDto != null){
                                messageAttachment.setPicUrl(weMediaDto.getUrl());
                            }
                            break;
                        case LINK:
                            messageAttachment.setTitle(msgObject.getString("title"));
                            messageAttachment.setPicUrl(msgObject.getString("picurl"));
                            messageAttachment.setDescription(msgObject.getString("desc"));
                            messageAttachment.setLinkUrl(msgObject.getString("url"));
                            break;
                        case MINIPROGRAM:
                            messageAttachment.setTitle(msgObject.getString("title"));
                            //messageAttachment.setMediaId(msgObject.getString("pic_media_id"));
                            messageAttachment.setMediaId(mediaId);
                            messageAttachment.setAppId(msgObject.getString("appid"));
                            messageAttachment.setLinkUrl(msgObject.getString("page"));
                            if(weMediaDto != null){
                                messageAttachment.setPicUrl(weMediaDto.getUrl());
                            }
                            break;
                        case VIDEO:
                        case FILE:
                            //messageAttachment.setMediaId(msgObject.getString("media_id"));
                            messageAttachment.setMediaId(mediaId);
                            if(weMediaDto != null){
                                messageAttachment.setFileUrl(weMediaDto.getUrl());
                            }
                            break;
                        default:
                            break;
                    }
                    attachmentList.add(messageAttachment);
                });
            }
        });
        groupMessageListService.saveOrUpdateBatch(saveGroupMessageList);
        attachmentsService.saveBatch(attachmentList);
    }

    /**
     * 企业群发成员执行结果
     *
     * @param groupMsgList
     */
    public void pullGroupMsgSendResult(WeGroupMsgListDto groupMsgList) {
        List<WeGroupMessageTask> taskList = new ArrayList<>();
        List<WeGroupMessageSendResult> sendResultlist = new ArrayList<>();
        Optional.ofNullable(groupMsgList).map(WeGroupMsgListDto::getGroupMsgList)
                .orElseGet(ArrayList::new).forEach(weGroupMsg -> {
            WeGroupMsgListDto groupMsgTask = groupMessageTaskService.getGroupMsgTask(weGroupMsg.getMsgId(), null);
            Optional.ofNullable(groupMsgTask).map(WeGroupMsgListDto::getTaskList).orElseGet(ArrayList::new).forEach(msgTask -> {
                WeGroupMessageTask messageTask = new WeGroupMessageTask();
                messageTask.setMsgId(weGroupMsg.getMsgId());
                messageTask.setUserId(msgTask.getUserId());
                if(msgTask.getSendTime() != null){
                    messageTask.setSendTime(new Date(msgTask.getSendTime() * 1000));
                }
                messageTask.setStatus(msgTask.getStatus());
                taskList.add(messageTask);

                WeGroupMsgListDto groupMsgSendResult = sendResultService.getGroupMsgSendResult(weGroupMsg.getMsgId(), msgTask.getUserId(), null);
                Optional.ofNullable(groupMsgSendResult).map(WeGroupMsgListDto::getSendList).orElseGet(ArrayList::new).forEach(sendResult -> {
                    WeGroupMessageSendResult messageSendResult = new WeGroupMessageSendResult();
                    messageSendResult.setMsgId(weGroupMsg.getMsgId());
                    messageSendResult.setUserId(msgTask.getUserId());
                    messageSendResult.setChatId(sendResult.getChatId());
                    messageSendResult.setExternalUserid(sendResult.getExternalUserId());
                    if(sendResult.getSendTime() != null){
                        messageSendResult.setSendTime(new Date(sendResult.getSendTime() * 1000));
                    }
                    messageSendResult.setStatus(sendResult.getStatus());
                    sendResultlist.add(messageSendResult);
                });
            });
        });
        groupMessageTaskService.addOrUpdateBatchByCondition(taskList);
        sendResultService.addOrUpdateBatchByCondition(sendResultlist);
    }


    public void groupMessageTaskHandler(){
        Long cacheZSetSize = redisCache.getCacheZSetSize(WeConstans.WEGROUPMSGTIMEDTASK_KEY);
        if(cacheZSetSize > 0){
            Set<ZSetOperations.TypedTuple<String>> typedTuples = redisCache.rangeWithScore(WeConstans.WEGROUPMSGTIMEDTASK_KEY, 0, 0);
            typedTuples.forEach(item -> {
                long score = Objects.requireNonNull(item.getScore()).longValue();
                if(score < System.currentTimeMillis()){
                    //当时间小于当前时间时删除第一个记录，并发送群发消息
                    log.info("获取到消息,执行群发任务: {}",item.getValue());
                    redisCache.removeCacheZSet(WeConstans.WEGROUPMSGTIMEDTASK_KEY,item.getValue());
                    WeAddGroupMessageQuery query = JSONObject.parseObject(item.getValue(), WeAddGroupMessageQuery.class);
                    List<WeAddGroupMessageQuery.SenderInfo> senderList = query.getSenderList();
                    List<WeGroupMessageList> weGroupMessageLists = new ArrayList<>();
                    WeGroupMessageList weGroupMessageList = groupMessageListService.getOne(new LambdaQueryWrapper<WeGroupMessageList>()
                            .eq(WeGroupMessageList::getMsgTemplateId, query.getId()).last("limit 1"));
                    if(weGroupMessageList != null && StringUtils.isEmpty(weGroupMessageList.getMsgId())){
                        senderList.forEach(sender ->{
                            WeAddMsgTemplateQuery templateQuery = new WeAddMsgTemplateQuery();
                            templateQuery.setChat_type(query.getChatType());
                            templateQuery.setSender(sender.getUserId());
                            if(ObjectUtil.equal(1,query.getChatType())){
                                templateQuery.setExternal_userid(sender.getCustomerList());
                            }
                            templateQuery.setAttachments(query.getAttachmentsList());
                            if(StringUtils.isNotEmpty(query.getContent())){
                                WeAddMsgTemplateQuery.Text text = new WeAddMsgTemplateQuery.Text();
                                text.setContent(query.getContent());
                                templateQuery.setText(text);
                            }
                            SendMessageResultDto resultDto = customerMessagePushClient.addMsgTemplate(templateQuery);
                            if(resultDto != null && ObjectUtil.equal(WeConstans.WE_SUCCESS_CODE,resultDto.getErrcode())){
                                String msgid = resultDto.getMsgid();
                                Long msgTemplateId = query.getId();
                                WeGroupMessageList messageList = new WeGroupMessageList();
                                messageList.setMsgId(msgid);
                                weGroupMessageLists.add(messageList);
                                groupMessageListService.update(messageList,new LambdaQueryWrapper<WeGroupMessageList>()
                                        .eq(WeGroupMessageList::getMsgTemplateId,msgTemplateId)
                                        .eq(WeGroupMessageList::getUserId,sender.getUserId()));
                            }
                        });
                    }
                }
            });
        }
    }
}
