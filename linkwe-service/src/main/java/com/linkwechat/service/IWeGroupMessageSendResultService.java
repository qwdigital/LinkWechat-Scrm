package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeGroupMessageSendResult;

import java.util.List;

/**
 * 群发消息成员执行结果表(WeGroupMessageSendResult)
 *
 * @author danmo
 * @since 2022-04-06 22:29:04
 */
public interface IWeGroupMessageSendResultService extends IService<WeGroupMessageSendResult> {

    /**
     * 查询列表
     */
    List<WeGroupMessageSendResult> queryList(WeGroupMessageSendResult weGroupMessageSendResult);

    void addOrUpdateBatchByCondition(List<WeGroupMessageSendResult> taskList);

    List<WeGroupMessageSendResult> groupMsgSendResultList(WeGroupMessageSendResult sendResult);
} 
