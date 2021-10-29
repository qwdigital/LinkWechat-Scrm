package com.linkwechat.wecom.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerMessagePushClient;
import com.linkwechat.wecom.domain.WeGroupMessageList;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgDto;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgListDto;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgTaskDto;
import com.linkwechat.wecom.domain.query.WeGetGroupMsgListQuery;
import com.linkwechat.wecom.domain.query.WeGroupMsgListQuery;
import com.linkwechat.wecom.mapper.WeGroupMessageListMapper;
import com.linkwechat.wecom.service.IWeGroupMessageListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 群发消息列Service业务层处理
 *
 * @author ruoyi
 * @date 2021-10-19
 */
@Service
public class WeGroupMessageListServiceImpl extends ServiceImpl<WeGroupMessageListMapper, WeGroupMessageList> implements IWeGroupMessageListService {

    @Autowired
    private WeCustomerMessagePushClient messagePushClient;

    @Override
    public List<WeGroupMessageList> queryList(WeGroupMessageList weGroupMessageList) {
        LambdaQueryWrapper<WeGroupMessageList> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(weGroupMessageList.getMsgId())) {
            lqw.eq(WeGroupMessageList::getMsgId, weGroupMessageList.getMsgId());
        }
        if (StringUtils.isNotBlank(weGroupMessageList.getChatType())) {
            lqw.eq(WeGroupMessageList::getChatType, weGroupMessageList.getChatType());
        }
        if (StringUtils.isNotBlank(weGroupMessageList.getUserId())) {
            lqw.eq(WeGroupMessageList::getUserId, weGroupMessageList.getUserId());
        }
        if (weGroupMessageList.getSendTime() != null) {
            lqw.eq(WeGroupMessageList::getSendTime, weGroupMessageList.getSendTime());
        }
        if (weGroupMessageList.getCreateType() != null) {
            lqw.eq(WeGroupMessageList::getCreateType, weGroupMessageList.getCreateType());
        }
        if (weGroupMessageList.getIsTask() != null) {
            lqw.eq(WeGroupMessageList::getIsTask, weGroupMessageList.getIsTask());
        }
        return this.list(lqw);
    }


    @Override
    public WeGroupMsgListDto getGroupMsgList(String chatType, Long startTime, Long endTime, String cursor){
        WeGroupMsgListQuery query = new WeGroupMsgListQuery();
        query.setChat_type(chatType);
        query.setStart_time(startTime);
        query.setEnd_time(endTime);
        WeGroupMsgListDto groupMsgList = messagePushClient.getGroupMsgList(query);
        if(groupMsgList != null && ObjectUtil.equal(0,groupMsgList.getErrcode())){
            if(StringUtils.isNotEmpty(groupMsgList.getNextCursor())){
                WeGroupMsgListDto clildMsgList = getGroupMsgList(query.getChat_type(),query.getStart_time(), query.getEnd_time(),groupMsgList.getNextCursor());
                List<WeGroupMsgDto> list = groupMsgList.getGroupMsgList();
                list.addAll(clildMsgList.getGroupMsgList());
                groupMsgList.setGroupMsgList(list);
                groupMsgList.setNextCursor(clildMsgList.getNextCursor());
            }
        }
        return groupMsgList;
    }

    @Override
    public WeGroupMsgListDto getGroupMsgTask(String msgId, String cursor) {
        WeGetGroupMsgListQuery query = new WeGetGroupMsgListQuery();
        query.setMsgId(msgId);
        query.setCursor(cursor);
        WeGroupMsgListDto groupMsgTask = messagePushClient.getGroupMsgTask(query);
        if(groupMsgTask != null && ObjectUtil.equal(0,groupMsgTask.getErrcode())){
            if(StringUtils.isNotEmpty(groupMsgTask.getNextCursor())){
                WeGroupMsgListDto clildMsgList = getGroupMsgTask(msgId,groupMsgTask.getNextCursor());
                List<WeGroupMsgTaskDto> taskList = clildMsgList.getTaskList();
                taskList.addAll(clildMsgList.getTaskList());
                groupMsgTask.setTaskList(taskList);
                groupMsgTask.setNextCursor(clildMsgList.getNextCursor());
            }
        }
        return groupMsgTask;
    }
}
