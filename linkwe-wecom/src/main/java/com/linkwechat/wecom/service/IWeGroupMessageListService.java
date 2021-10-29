package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeGroupMessageList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgListDto;

import java.util.List;

/**
 * 群发消息列Service接口
 *
 * @author ruoyi
 * @date 2021-10-19
 */
public interface IWeGroupMessageListService extends IService<WeGroupMessageList> {

    /**
     * 查询列表
     */
    List<WeGroupMessageList> queryList(WeGroupMessageList weGroupMessageList);

    /**
     * 获取群发记录列表
     *
     * @param chatType 群发任务的类型，single，表示发送给客户，group表示发送给客户群
     * @param startTime 群发任务记录开始时间
     * @param endTime 群发任务记录结束时间
     * @param cursor 用于分页查询的游标
     * @return WeGroupMsgListDto 群发记录列表
     */
    WeGroupMsgListDto getGroupMsgList(String chatType, Long startTime, Long endTime, String cursor);

    /**
     * 获取群发成员发送任务列表
     *
     * @param msgId 群发消息的id
     * @param cursor 用于分页查询的游标
     * @return WeGroupMsgListDto 群发成员发送任务列表
     */
    WeGroupMsgListDto getGroupMsgTask(String msgId, String cursor);
}
