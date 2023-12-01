package com.linkwechat.service;

import com.linkwechat.domain.WeAiMsgQuery;

public interface IWeAiSessionService {
    void createSseConnect();


    void closeSseConnect(String sessionId);

    void sendMsg(WeAiMsgQuery query);

    void sendAiMsg(WeAiMsgQuery query);
}
