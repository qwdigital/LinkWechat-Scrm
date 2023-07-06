package com.linkwechat.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 素材中心 websocket服务器
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/05 15:57
 */
@Slf4j
@Component
public class WeMaterialWebSocketServer implements WebSocketHandler {


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("有新连接加入！ sessionId：" + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("有连接关闭！ sessionId：{}，" + session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
