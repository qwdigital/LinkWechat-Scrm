package com.linkwechat.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.CacheConstants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.SecurityConstants;
import com.linkwechat.common.constant.TokenConstants;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.domain.model.WxLoginUser;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.JwtUtils;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.gateway.config.properties.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 网关鉴权
 *
 * @author leejoker
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    // 排除过滤的 uri 地址，nacos自行添加
    @Resource
    private IgnoreWhiteProperties ignoreWhite;

    @Resource
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (StringUtils.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }
        try {
            Claims claims = JwtUtils.parseToken(token);
            if (claims == null) {
                return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
            }
            String userKey = JwtUtils.getUserKey(claims);
            boolean islogin = redisService.hasKey(getTokenKey(userKey));
            if (!islogin) {
                return unauthorizedResponse(exchange, "登录状态已过期");
            }

            /** 登陆类型 **/
            //之后改为策略模式
            String loginType = JwtUtils.getValue(claims, SecurityConstants.LOGIN_TYPE);
            addHeader(mutate, SecurityConstants.LOGIN_TYPE, loginType);
            if (StringUtils.isNotEmpty(loginType) && loginType.equals("LinkWeChatWXAPI")) {
                WxLoginUser wxLoginUser = getWxLoginUser(token);
                // 设置用户信息到请求
                addHeader(mutate, SecurityConstants.LOGIN_USER, JSONObject.toJSONString(wxLoginUser));
            } else {
                String corpId = JwtUtils.getCorpId(claims);
                String corpName = JwtUtils.getCorpName(claims);
                String userId = JwtUtils.getUserId(claims);
                String userName = JwtUtils.getUserName(claims);
                String userType = JwtUtils.getUserType(claims);

                LoginUser loginUser = getLoginUser(token);
                //String loginUser = JwtUtils.getLoginUser(claims);
                // 设置用户信息到请求
                addHeader(mutate, SecurityConstants.CORP_ID, corpId);
                addHeader(mutate, SecurityConstants.CORP_NAME, corpName);
                addHeader(mutate, SecurityConstants.USER_ID, userId);
                addHeader(mutate, SecurityConstants.USER_NAME, userName);
                addHeader(mutate, SecurityConstants.USER_TYPE, userType);
                addHeader(mutate, SecurityConstants.USER_KEY, userKey);
                addHeader(mutate, SecurityConstants.LOGIN_USER, JSONObject.toJSONString(loginUser));
            }
        } catch (Exception e) {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }

        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtils.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{},msg:{}", exchange.getRequest().getPath(), msg);
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String token) {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);

        if (StrUtil.isBlank(token)) {
            //判断是否是websocket链接
            String connection = request.getHeaders().getFirst("Connection");
            String upgrade = request.getHeaders().getFirst("Upgrade");
            if ("Upgrade".equals(connection) && "websocket".equals(upgrade)) {
                token = request.getHeaders().getFirst("Sec-WebSocket-Protocol");
            }
        }

        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }

        return token;
    }

    @Override
    public int getOrder() {
        return -200;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userKey = JwtUtils.getUserKey(token);
                return redisService.getCacheObject(getTokenKey(userKey));
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public WxLoginUser getWxLoginUser(String token) {
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userKey = JwtUtils.getUserKey(token);
                return redisService.getCacheObject(getTokenKey(userKey));
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}