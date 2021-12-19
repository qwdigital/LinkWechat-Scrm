package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeMessagePush;

import java.util.List;

/**
 * 消息发送的Service接口
 *
 * @author KeWen
 * @date 2020-10-28
 */
public interface IWeMessagePushService {
    /**
     * 查询消息发送的
     *
     * @param messagePushId 消息发送的ID
     * @return 消息发送的
     */
    public WeMessagePush selectWeMessagePushById(Long messagePushId);

    /**
     * 查询消息发送的列表
     *
     * @param weMessagePush 消息发送的
     * @return 消息发送的集合
     */
    public List<WeMessagePush> selectWeMessagePushList(WeMessagePush weMessagePush);

    /**
     * 新增消息发送的
     *
     * @param weMessagePush 消息发送的
     * @return 结果
     */
    public void insertWeMessagePush(WeMessagePush weMessagePush);

    /**
     * 批量删除消息发送的
     *
     * @param messagePushIds 需要删除的消息发送的ID
     * @return 结果
     */
    public int deleteWeMessagePushByIds(Long[] messagePushIds);

    /**
     * 删除消息发送的信息
     *
     * @param messagePushId 消息发送的ID
     * @return 结果
     */
    public int deleteWeMessagePushById(Long messagePushId);


    /**
     * 推送信息给员工附带自己的url链接，跳转H5
     */
    public void pushMessageSelfH5(List<String> toUser,String textContent,Integer taskType);
}
