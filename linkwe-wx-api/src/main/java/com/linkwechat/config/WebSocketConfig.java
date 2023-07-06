package com.linkwechat.config;

import com.linkwechat.websocket.WeMaterialWebSocketServer;
import com.linkwechat.interceptor.WebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;

/**
 * WebSocket配置类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/05 16:00
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Resource
    private WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;
    @Resource
    private WeMaterialWebSocketServer weMaterialWebSocketServer;

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(weMaterialWebSocketServer,"/info").addInterceptors(webSocketHandshakeInterceptor).setAllowedOrigins("*");
    }
}
