package com.linkwechat.gateway.handler;

import com.linkwechat.common.constant.CacheConstants;
import com.linkwechat.common.constant.TokenConstants;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.JwtUtils;
import com.linkwechat.common.utils.StringUtils;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author leejoker
 * @date 2022/5/2 23:12
 */
@Component
public class LogoutHandler implements HandlerFunction<ServerResponse> {
    @Resource
    private RedisService redisService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        String token = request.headers().asHttpHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        Claims claims = JwtUtils.parseToken(token);
        String userKey = JwtUtils.getUserKey(claims);
        boolean islogin = redisService.hasKey(CacheConstants.LOGIN_TOKEN_KEY + userKey);
        if (islogin) {
            redisService.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + userKey);
        }
        return ServerResponse.status(org.springframework.http.HttpStatus.OK).build();
    }
}
