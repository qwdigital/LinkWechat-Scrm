package com.linkwechat.service;

import com.github.pagehelper.PageInfo;
import com.linkwechat.domain.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface IWeAiSessionService {
    SseEmitter createSseConnect(String sessionId);


    void closeSseConnect(String sessionId);

    void sendMsg(WeAiMsgQuery query);

    void sendAiMsg(WeAiMsgQuery query);

    PageInfo<WeAiMsgVo> list(WeAiMsgListQuery query);

    List<WeAiMsgVo> getDetail(String sessionId);

    /**
     * 创建连接并发送消息
     * @param query
     * @return
     */
    SseEmitter createAndSendMsg(WeAiMsgQuery query);

    /**
     * 删除会话
     * @param query
     */
    void delMsg(WeAiMsgQuery query);

    /**
     * 收藏/取消消息
     * @param query
     * @return
     */
    void collectionMsg(WeAiCollectionMsgQuery query);


    /**
     * 收藏列表
     * @param query
     * @return
     */
    PageInfo<WeAiCollectionMsgVo> collectionList(WeAiMsgListQuery query);
}
