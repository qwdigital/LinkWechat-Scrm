package com.linkwechat.service;

import com.linkwechat.domain.WeAiMsgQuery;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface IWeAiSessionService {
    SseEmitter createSseConnect();


    void closeSseConnect(String sessionId);

    void sendMsg(WeAiMsgQuery query);

    void sendAiMsg(WeAiMsgQuery query);
}
