package com.linkwechat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * 素材中心 websocket服务器
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/05 15:57
 */
@Slf4j
@Component
@ServerEndpoint("/info")
public class WeMaterialWebSocketServer {
    @OnOpen
    public void onOpen(Session session) {
        log.info("客户端：{}连接成功",session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        log.info("客户端：{}连接断开",session.getId());

    }

    @OnMessage
    public String onMsg(String message,Session session) {
        log.info("从客户端：{} 收到<--:{}", session.getId(),message);
        String send=message.toUpperCase();
        String result="客户：%s您好，来自server 的消息:%s";
        result = String.format(result, session.getId(), send);
        return "来自server 的消息：" + result;
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

}
