package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeGroupMessageSendResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeGroupMessageTask;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgListDto;

import java.util.List;

/**
 * 群发消息成员执行结果Service接口
 *
 * @author ruoyi
 * @date 2021-10-19
 */
public interface IWeGroupMessageSendResultService extends IService<WeGroupMessageSendResult> {

    /**
     * 查询列表
     */
    List<WeGroupMessageSendResult> queryList(WeGroupMessageSendResult weGroupMessageSendResult);

    /**
     * 获取企业群发成员执行结果
     *
     * @param msgId 群发消息的id
     * @param userId 发送成员userid
     * @param cursor 用于分页查询的游标
     * @return WeGroupMsgListDto 群成员发送结果列表
     */
    WeGroupMsgListDto getGroupMsgSendResult(String msgId, String userId, String cursor);


    void addOrUpdateBatchByCondition(List<WeGroupMessageSendResult> taskList);

    List<WeGroupMessageSendResult> groupMsgSendResultList(WeGroupMessageSendResult sendResult);
}
