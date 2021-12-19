package com.linkwechat.quartz.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeGroupMessageAttachments;
import com.linkwechat.wecom.domain.WeGroupMessageList;
import com.linkwechat.wecom.domain.WeGroupMessageSendResult;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgListDto;
import com.linkwechat.wecom.domain.query.WeAddMsgTemplateQuery;
import com.linkwechat.wecom.service.IWeGroupMessageAttachmentsService;
import com.linkwechat.wecom.service.IWeGroupMessageListService;
import com.linkwechat.wecom.service.IWeGroupMessageSendResultService;
import com.linkwechat.wecom.service.IWeGroupMessageTaskService;
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
    private IWeGroupMessageListService groupMessageListService;

    @Autowired
    private IWeGroupMessageAttachmentsService attachmentsService;

    @Autowired
    private IWeGroupMessageSendResultService sendResultService;

    @Autowired
    private IWeGroupMessageTaskService iWeGroupMessageTaskService;

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
            ThreadUtil.execAsync(this::pullSingleMsgList);
            return;
        } else if (ObjectUtil.equal("4", type)) {
            startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -30)).getTime() / 1000;
            endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
            ThreadUtil.execAsync(this::pullGroupMsgList);
            return;
        } else if (ObjectUtil.equal("5", type)) {
            startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
            endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
            ThreadUtil.execAsync(this::pullSingleMsgList);
            return;
        } else if (ObjectUtil.equal("6", type)) {
            startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
            endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
            ThreadUtil.execAsync(this::pullGroupMsgList);
            return;
        }
        ThreadUtil.execAsync(this::pullSingleMsgList);
        ThreadUtil.execAsync(this::pullGroupMsgList);
    }

    //拉取发送给客户的群发消息
    private void pullSingleMsgList() {
        WeGroupMsgListDto singleMsgList = groupMessageListService.getGroupMsgList("single", startTime, endTime, null);
        saveGroupMsg(singleMsgList);
        pullGroupMsgSendResult(singleMsgList);

    }

    //拉取发送给客户群的群发消息
    private void pullGroupMsgList() {
        //拉取发送给客户的群发消息
        WeGroupMsgListDto groupMsgList = groupMessageListService.getGroupMsgList("group", startTime, endTime, null);
        saveGroupMsg(groupMsgList);
        pullGroupMsgSendResult(groupMsgList);
    }

    //保存消息入库
    private void saveGroupMsg(WeGroupMsgListDto groupMsgList) {
        Optional.ofNullable(groupMsgList).map(WeGroupMsgListDto::getGroupMsgList)
                .orElseGet(ArrayList::new).forEach(weGroupMsg -> {
            WeGroupMessageList weGroupMessageList = new WeGroupMessageList();
            weGroupMessageList.setMsgId(weGroupMsg.getMsgId());
            weGroupMessageList.setChatType("single");
            weGroupMessageList.setCreateType(weGroupMsg.getCreateType());
            weGroupMessageList.setSendTime(new Date(weGroupMsg.getCreateTime() * 1000));
            weGroupMessageList.setUserId(weGroupMsg.getCreator());
            WeGroupMessageList weGroupMessage = groupMessageListService.getOne(new LambdaQueryWrapper<WeGroupMessageList>()
                    .eq(WeGroupMessageList::getMsgId, weGroupMsg.getMsgId()).last("limit 1"));
            if (weGroupMessage != null) {
                weGroupMessageList.setId(weGroupMessage.getId());
                groupMessageListService.updateById(weGroupMessageList);
            } else {
                if (groupMessageListService.save(weGroupMessageList)) {
                    List<WeGroupMessageAttachments> attachmentList = new ArrayList<>();
                    String content = Optional.ofNullable(weGroupMsg.getText()).map(WeAddMsgTemplateQuery.Text::getContent).orElse("");
                    if (StringUtils.isNotEmpty(content)) {
                        WeGroupMessageAttachments attachment = new WeGroupMessageAttachments();
                        attachment.setMsgType("text");
                        attachment.setContent(content);
                        attachmentList.add(attachment);
                    }
                    List<JSONObject> attachments = weGroupMsg.getAttachments();
                    Optional.ofNullable(attachments).orElseGet(ArrayList::new).forEach(attachment -> {
                        String msgtype = attachment.getString("msgtype");
                        JSONObject msgObject = attachment.getJSONObject(msgtype);
                        WeGroupMessageAttachments messageAttachment = new WeGroupMessageAttachments();
                        messageAttachment.setMsgType(msgtype);

                        MessageType messageType = MessageType.valueOf(msgtype);
                        switch (messageType) {
                            case IMAGE:
                                messageAttachment.setMediaId(msgObject.getString("media_id"));
                                messageAttachment.setPicUrl(msgObject.getString("pic_url"));
                                break;
                            case LINK:
                                messageAttachment.setTitle(msgObject.getString("title"));
                                messageAttachment.setPicUrl(msgObject.getString("picurl"));
                                messageAttachment.setDescription(msgObject.getString("desc"));
                                messageAttachment.setLinkUrl(msgObject.getString("url"));
                                break;
                            case MINIPROGRAM:
                                messageAttachment.setTitle(msgObject.getString("title"));
                                messageAttachment.setMediaId(msgObject.getString("pic_media_id"));
                                messageAttachment.setAppId(msgObject.getString("appid"));
                                messageAttachment.setLinkUrl(msgObject.getString("page"));
                                break;
                            case VIDEO:
                            case FILE:
                                messageAttachment.setMediaId(msgObject.getString("media_id"));
                                break;
                            default:
                                break;
                        }
                        attachmentList.add(messageAttachment);
                    });
                    attachmentsService.saveBatch(attachmentList);
                }
            }
        });
    }

    /**
     * 企业群发成员执行结果
     *
     * @param groupMsgList
     */
    public void pullGroupMsgSendResult(WeGroupMsgListDto groupMsgList) {
        List<WeGroupMessageSendResult> list = new ArrayList<>();
        Optional.ofNullable(groupMsgList).map(WeGroupMsgListDto::getGroupMsgList)
                .orElseGet(ArrayList::new).forEach(weGroupMsg -> {
            WeGroupMsgListDto groupMsgTask = iWeGroupMessageTaskService.getGroupMsgTask(weGroupMsg.getMsgId(), null);
            Optional.ofNullable(groupMsgTask).map(WeGroupMsgListDto::getTaskList).orElseGet(ArrayList::new).forEach(msgTask -> {
                WeGroupMsgListDto groupMsgSendResult = sendResultService.getGroupMsgSendResult(weGroupMsg.getMsgId(), msgTask.getUserId(), null);
                Optional.ofNullable(groupMsgSendResult).map(WeGroupMsgListDto::getSendList).orElseGet(ArrayList::new).forEach(sendResult -> {
                    WeGroupMessageSendResult messageSendResult = new WeGroupMessageSendResult();
                    messageSendResult.setMsgId(weGroupMsg.getMsgId());
                    messageSendResult.setUserId(msgTask.getUserId());
                    messageSendResult.setChatId(sendResult.getChatId());
                    messageSendResult.setExternalUserid(sendResult.getExternalUserId());
                    messageSendResult.setSendTime(new Date(sendResult.getSendTime() * 1000));
                    messageSendResult.setStatus(sendResult.getStatus());
                    list.add(messageSendResult);
                });
            });
        });
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(item -> {
                if (!sendResultService.update(item, new LambdaQueryWrapper<WeGroupMessageSendResult>()
                        .eq(WeGroupMessageSendResult::getMsgId, item.getMsgId())
                        .eq(WeGroupMessageSendResult::getExternalUserid, item.getExternalUserid())
                        .eq(WeGroupMessageSendResult::getUserId, item.getUserId())
                        .eq(WeGroupMessageSendResult::getChatId, item.getChatId()))) {
                    sendResultService.save(item);
                }
            });
        }
    }
}
