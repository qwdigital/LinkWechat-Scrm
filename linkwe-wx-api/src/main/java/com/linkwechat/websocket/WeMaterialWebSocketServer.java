package com.linkwechat.websocket;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.material.query.WeContentViewRecordQuery;
import com.linkwechat.domain.material.query.WeMaterialMobileAddViewRequest;
import com.linkwechat.service.IWeContentViewRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    @Resource
    private IWeContentViewRecordService weContentViewRecordService;

    /**
     * 空对象
     */
    private final WeMaterialMobileAddViewRequest EMPTY = new WeMaterialMobileAddViewRequest();

    /**
     * ConcurrentHashMap线程安全Map
     */
    private static Map<String, WeMaterialMobileAddViewRequest> weSocketMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        weSocketMap.put(session.getId(), EMPTY);
        log.info("新的连接进来,sessionId为：{}，当前连接人数：{}", session.getId(), weSocketMap.size());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Object payload = message.getPayload();
        WeMaterialMobileAddViewRequest request = JSONObject.parseObject(payload.toString(), WeMaterialMobileAddViewRequest.class);
        if (request.getType().equals(0)) {
            log.info("接收到心跳：sessionId为{}", session.getId());
        } else if (request.getType().equals(1)) {
            request.setViewStartTime(new Date());
            weSocketMap.put(session.getId(), request);
            log.info("接收到消息：{}", JSONObject.toJSONString(request));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        WeMaterialMobileAddViewRequest request = weSocketMap.get(session.getId());
        //业务处理
        businessHandler(request);
        weSocketMap.remove(session.getId());
        log.info("连接关闭,sessionId为：{}，当前连接人数：{}", session.getId(), weSocketMap.size());
    }


    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 业务处理
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/07/06 11:04
     */
    private void businessHandler(WeMaterialMobileAddViewRequest request) {

        log.info("链接断开，业务数据：{}", JSONObject.toJSONString(request));

        //如果其中有一个为null，该条数据不处理
        if (StringUtils.isEmpty(request.getSendUserId()) || StringUtils.isEmpty(request.getOpenid())
                || StringUtils.isEmpty(request.getUnionid()) || "null".equals(request.getOpenid()) || "null".equals(request.getUnionid())) {
            return;
        }

        WeContentViewRecordQuery query = new WeContentViewRecordQuery();
        if (StringUtils.isNotBlank(request.getTalkId())) {
            query.setTalkId(Long.valueOf(request.getTalkId()));
        }
        query.setContentId(Long.valueOf(request.getContentId()));
        query.setOpenid(request.getOpenid());
        query.setUnionid(request.getUnionid());
        query.setSendUserId(Long.valueOf(request.getSendUserId()));
        query.setViewTime(new Date());
        long intervalTime = DateUtil.date().getTime() - request.getViewStartTime().getTime();
        query.setViewWatchTime(Long.valueOf(intervalTime));
        weContentViewRecordService.addView(query);
    }
}
