package com.linkwechat.gateway.config;

import com.linkwechat.gateway.handler.LogoutHandler;
import com.linkwechat.gateway.handler.ServerStateHandler;
import com.linkwechat.gateway.handler.ValidateCodeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import javax.annotation.Resource;

/**
 * 路由配置信息
 *
 * @author leejoker
 */
@Configuration
public class RouterFunctionConfiguration {
    @Resource
    private ValidateCodeHandler validateCodeHandler;

    @Resource
    private LogoutHandler logoutHandler;

    @Resource
    private ServerStateHandler serverStateHandler;

    @SuppressWarnings("rawtypes")
    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions.route(RequestPredicates.GET("/code").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                validateCodeHandler);
    }

    @Bean
    public RouterFunction logoutFunction() {
        return RouterFunctions.route(
                RequestPredicates.POST("/logout").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), logoutHandler);
    }


    @Bean
    public RouterFunction serverStateFunction() {
        return RouterFunctions.route(RequestPredicates.GET("/serverState").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                serverStateHandler);
    }
}
