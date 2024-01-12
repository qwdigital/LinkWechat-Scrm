package com.linkwechat.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author danmo
 * @date 2023年09月22日 10:53
 */
@Slf4j
public class WeAiSessionUtil {

    public static Map<String, SseEmitter> sessionPool = new ConcurrentHashMap<>(16);



    /**
     * 添加session
     *
     * @param key     sessionId
     * @param session session
     */
    public static void add(String key, SseEmitter session) {
        sessionPool.put(key, session);
    }

    /**
     * 移除session
     *
     * @param key sessionId
     */
    public static void remove(String key) {
        sessionPool.remove(key);
    }

    /**
     * 移除session并关闭连接
     *
     * @param key sessionId
     */
    public static void removeAndClose(String key) {
        if (sessionPool.containsKey(key)) {
            SseEmitter sseEmitter = sessionPool.remove(key);
            if(Objects.nonNull(sseEmitter)){
                sseEmitter.complete();
            }
        }

    }

    /**
     * 获取session
     *
     * @param key sessionId
     * @return
     */
    public static SseEmitter get(String key) {
        return sessionPool.get(key);
    }
}
