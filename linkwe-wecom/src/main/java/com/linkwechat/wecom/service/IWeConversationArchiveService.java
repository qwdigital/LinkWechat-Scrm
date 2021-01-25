package com.linkwechat.wecom.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.core.domain.ConversationArchiveQuery;

import java.util.List;

/**
 * @author danmo
 * @description 会话存档业务接口
 * @date 2020/12/19 13:59
 **/
public interface IWeConversationArchiveService {
    /**
     * 根据用户ID 获取对应内部联系人列表
     * @param query 入参
     * @param /fromId 发送人id
     * @param /reveiceId 接收人id
     * @return
     */
    PageInfo<JSONObject> getChatContactList(ConversationArchiveQuery query);

    PageInfo<JSONObject> getChatRoomContactList(ConversationArchiveQuery query);

    /**
     * 查询最早聊天记录
     * @param fromId
     * @param receiveId
     * @return
     */
    JSONObject getFinalChatContactInfo(String fromId, String receiveId);

    /**
     * 查询最早群聊记录
     * @param fromId
     * @param roomId
     * @return
     */
    JSONObject getFinalChatRoomContactInfo(String fromId, String roomId);

    /**
     * 获取全局会话数据接口
     *
     * @param query 参
     * @return
     */
    PageInfo<JSONObject> getChatAllList(ConversationArchiveQuery query);
}

