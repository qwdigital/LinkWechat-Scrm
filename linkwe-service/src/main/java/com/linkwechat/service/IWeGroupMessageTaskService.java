package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeGroupMessageTask;

import java.util.List;

/**
 * 群发消息成员发送任务表(WeGroupMessageTask)
 *
 * @author danmo
 * @since 2022-04-06 22:29:05
 */
public interface IWeGroupMessageTaskService extends IService<WeGroupMessageTask> {


    /**
     * 根据消息id和成员id批量保存更新
     * @param taskList
     */
    void addOrUpdateBatchByCondition(List<WeGroupMessageTask> taskList);

    List<WeGroupMessageTask> groupMsgTaskList(WeGroupMessageTask task);
} 
