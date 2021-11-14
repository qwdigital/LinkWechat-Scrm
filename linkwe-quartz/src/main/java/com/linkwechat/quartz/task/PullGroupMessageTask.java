package com.linkwechat.quartz.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgListDto;
import com.linkwechat.wecom.domain.query.WeAddMsgTemplateQuery;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author danmo
 * @description 消息群发任务列表拉取
 * @date 2021/10/15 22:59
 **/
@Slf4j
@Component("pullGroupMessageTask")
public class PullGroupMessageTask {

    @Autowired
    private IWeGroupMessageTemplateService groupMessageTemplateService;

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
        pullGroupMsgList();
        pullSingleMsgList();
    }

    //拉取发送给客户的群发消息
    private void pullSingleMsgList() {
        WeGroupMsgListDto singleMsgList = groupMessageListService.getGroupMsgList("single", startTime, endTime, null);
        List<WeGroupMessageList> weGroupMessageLists = saveGroupMsg(singleMsgList, "single");
        pullGroupMsgSendResult(weGroupMessageLists);

    }

    //拉取发送给客户群的群发消息
    private void pullGroupMsgList() {
        //拉取发送给客户的群发消息
        WeGroupMsgListDto groupMsgList = groupMessageListService.getGroupMsgList("group", startTime, endTime, null);
        List<WeGroupMessageList> weGroupMessageLists = saveGroupMsg(groupMsgList, "group");
        pullGroupMsgSendResult(weGroupMessageLists);
    }

    /**
     * 保存消息入库
     *
     * @param groupMsgList
     * @param chatType
     */
    private List<WeGroupMessageList> saveGroupMsg(WeGroupMsgListDto groupMsgList, String chatType) {
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
                List<WeGroupMessageAttachments> attachmentTempList = new ArrayList<>();
                WeGroupMessageTemplate weGroupMessageTemplate = new WeGroupMessageTemplate();
                if (ObjectUtil.equal("single", chatType)) {
                    weGroupMessageTemplate.setChatType(1);
                } else {
                    weGroupMessageTemplate.setChatType(2);
                }
                weGroupMessageTemplate.setSendTime(weGroupMessageList.getSendTime());
                weGroupMessageTemplate.setStatus(1);
                weGroupMessageTemplate.setIsTask(0);

                String content = Optional.ofNullable(weGroupMsg.getText()).map(WeAddMsgTemplateQuery.Text::getContent).orElse("");
                if (StringUtils.isNotEmpty(content)) {
                    weGroupMessageTemplate.setContent(content);
                    WeGroupMessageAttachments attachment = new WeGroupMessageAttachments();
                    attachment.setMsgId(weGroupMsg.getMsgId());
                    attachment.setMsgType("text");
                    attachment.setContent(content);
                    attachmentTempList.add(attachment);
                }
                List<JSONObject> attachments = weGroupMsg.getAttachments();
                Optional.ofNullable(attachments).orElseGet(ArrayList::new).forEach(attachment -> {
                    String msgtype = attachment.getString("msgtype");
                    JSONObject msgObject = attachment.getJSONObject(msgtype);
                    WeGroupMessageAttachments messageAttachment = new WeGroupMessageAttachments();
                    messageAttachment.setMsgId(weGroupMsg.getMsgId());
                    messageAttachment.setMsgType(msgtype);
                    String mediaId = msgObject.getString("media_id") == null ? msgObject.getString("pic_media_id")
                            : msgObject.getString("media_id");
                    WeMediaDto weMediaDto = null;
                    if (StringUtils.isNotEmpty(mediaId)) {
                        try {
                            weMediaDto = weMaterialService.getMediaToResponse(mediaId);
                        } catch (Exception e) {
                            log.info("获取素材信息失败.........." + e.getMessage());
                        }
                    }
                    MessageType messageType = MessageType.messageTypeOf(msgtype);
                    switch (messageType) {
                        case IMAGE:
                            //messageAttachment.setMediaId(msgObject.getString("media_id"));
                            messageAttachment.setMediaId(mediaId);
                            messageAttachment.setPicUrl(msgObject.getString("pic_url"));
                            if (weMediaDto != null) {
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
                            if (weMediaDto != null) {
                                messageAttachment.setPicUrl(weMediaDto.getUrl());
                            }
                            break;
                        case VIDEO:
                        case FILE:
                            //messageAttachment.setMediaId(msgObject.getString("media_id"));
                            messageAttachment.setMediaId(mediaId);
                            if (weMediaDto != null) {
                                messageAttachment.setFileUrl(weMediaDto.getUrl());
                            }
                            break;
                        default:
                            break;
                    }
                    attachmentTempList.add(messageAttachment);
                });
                if (groupMessageTemplateService.save(weGroupMessageTemplate)) {
                    weGroupMessageList.setMsgTemplateId(weGroupMessageTemplate.getId());
                    saveGroupMessageList.add(weGroupMessageList);
                    attachmentTempList.forEach(item -> item.setMsgTemplateId(weGroupMessageTemplate.getId()));
                    attachmentList.addAll(attachmentTempList);
                }
            }
        });
        groupMessageListService.saveOrUpdateBatch(saveGroupMessageList);
        attachmentsService.saveBatch(attachmentList);
        return saveGroupMessageList;
    }

    /**
     * 企业群发成员执行结果
     *
     * @param weGroupMessageLists
     */
    public void pullGroupMsgSendResult(List<WeGroupMessageList> weGroupMessageLists) {
        List<WeGroupMessageTask> taskList = new ArrayList<>();
        List<WeGroupMessageSendResult> sendResultlist = new ArrayList<>();
        Optional.ofNullable(weGroupMessageLists).orElseGet(ArrayList::new).forEach(weGroupMsg -> {
            WeGroupMsgListDto groupMsgTask = groupMessageTaskService.getGroupMsgTask(weGroupMsg.getMsgId(), null);
            Optional.ofNullable(groupMsgTask).map(WeGroupMsgListDto::getTaskList).orElseGet(ArrayList::new).forEach(msgTask -> {
                WeGroupMessageTask messageTask = new WeGroupMessageTask();
                messageTask.setMsgId(weGroupMsg.getMsgId());
                messageTask.setUserId(msgTask.getUserId());
                if (msgTask.getSendTime() != null) {
                    messageTask.setSendTime(new Date(msgTask.getSendTime() * 1000));
                }
                messageTask.setStatus(msgTask.getStatus());
                messageTask.setMsgTemplateId(weGroupMsg.getMsgTemplateId());
                taskList.add(messageTask);

                WeGroupMsgListDto groupMsgSendResult = sendResultService.getGroupMsgSendResult(weGroupMsg.getMsgId(), msgTask.getUserId(), null);
                Optional.ofNullable(groupMsgSendResult).map(WeGroupMsgListDto::getSendList).orElseGet(ArrayList::new).forEach(sendResult -> {
                    WeGroupMessageSendResult messageSendResult = new WeGroupMessageSendResult();
                    messageSendResult.setMsgId(weGroupMsg.getMsgId());
                    messageSendResult.setUserId(msgTask.getUserId());
                    messageSendResult.setChatId(sendResult.getChatId());
                    messageSendResult.setExternalUserid(sendResult.getExternalUserId());
                    if (sendResult.getSendTime() != null) {
                        messageSendResult.setSendTime(new Date(sendResult.getSendTime() * 1000));
                    }
                    messageSendResult.setStatus(sendResult.getStatus());
                    messageSendResult.setMsgTemplateId(weGroupMsg.getMsgTemplateId());
                    sendResultlist.add(messageSendResult);
                });
            });
        });
        groupMessageTaskService.addOrUpdateBatchByCondition(taskList);
        sendResultService.addOrUpdateBatchByCondition(sendResultlist);
    }
}
