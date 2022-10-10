package com.linkwechat.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 日志打印
 *
 * @author danmo
 * @date 2021年09月26日 17:39
 */

//@Component
public class LogFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        StringBuilder logBuilder = new StringBuilder();
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String method = serverHttpRequest.getMethodValue().toUpperCase();
        MediaType mediaType =  serverHttpRequest.getHeaders().getContentType();
        logBuilder.append(method).append(",").append(serverHttpRequest.getURI());
        return null;
    }

    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 判断是否是上传文件
     * @param mediaType MediaType
     * @return Boolean
     */
    public static boolean isUploadFile(MediaType mediaType) {
        if (Objects.isNull(mediaType)) {
            return false;
        }
        return mediaType.equals(MediaType.MULTIPART_FORM_DATA)
                || mediaType.equals(MediaType.IMAGE_GIF)
                || mediaType.equals(MediaType.IMAGE_JPEG)
                || mediaType.equals(MediaType.IMAGE_PNG)
                || mediaType.equals(MediaType.MULTIPART_MIXED);
    }
}
