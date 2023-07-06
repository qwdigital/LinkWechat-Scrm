package com.linkwechat.interceptor;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * WebSocket握手拦截器
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/06 10:00
 */
@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();

        //当Sec-WebSocket-Protocol请求头不为空时,需要返回给前端相同的响应,否则链接会中断
        String header = httpRequest.getHeader("Sec-WebSocket-Protocol");
        if (StrUtil.isNotBlank(header)) {
            httpResponse.addHeader("Sec-WebSocket-Protocol", header);
        }
    }
}
