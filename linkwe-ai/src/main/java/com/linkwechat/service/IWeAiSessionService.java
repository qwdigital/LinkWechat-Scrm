package com.linkwechat.service;

import com.github.pagehelper.PageInfo;
import com.linkwechat.domain.WeAiMsgListQuery;
import com.linkwechat.domain.WeAiMsgQuery;
import com.linkwechat.domain.WeAiMsgVo;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface IWeAiSessionService {
    SseEmitter createSseConnect(String sessionId);


    void closeSseConnect(String sessionId);

    void sendMsg(WeAiMsgQuery query);

    void sendAiMsg(WeAiMsgQuery query);

    PageInfo<WeAiMsgVo> list(WeAiMsgListQuery query);

    List<WeAiMsgVo> getDetail(String sessionId);


    SseEmitter createAndSendMsg(WeAiMsgQuery query);

    void collectionMsg(Long id);
}
