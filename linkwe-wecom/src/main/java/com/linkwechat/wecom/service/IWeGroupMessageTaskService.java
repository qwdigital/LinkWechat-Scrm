package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeGroupMessageSendResult;
import com.linkwechat.wecom.domain.WeGroupMessageTask;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgListDto;

import java.util.List;

/**
 * 群发消息成员发送任务表Service接口
 *
 * @author ruoyi
 * @date 2021-10-19
 */
public interface IWeGroupMessageTaskService extends IService<WeGroupMessageTask> {
    /**
     * 获取群发成员发送任务列表
     *
     * @param msgId 群发消息的id
     * @param cursor 用于分页查询的游标
     * @return WeGroupMsgListDto 群发成员发送任务列表
     */
    WeGroupMsgListDto getGroupMsgTask(String msgId, String cursor);

    /**
     * 根据消息id和成员id批量保存更新
     * @param taskList
     */
    void addOrUpdateBatchByCondition(List<WeGroupMessageTask> taskList);

    List<WeGroupMessageTask> groupMsgTaskList(WeGroupMessageTask task);
}
