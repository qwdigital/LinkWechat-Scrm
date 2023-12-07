package com.linkwechat.common.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.SecurityConstants;
import com.linkwechat.common.constant.TokenConstants;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Enumeration;

/**
 * feign 请求拦截器
 *
 * @author danmo
 */
@Slf4j
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {


        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            String name;
            // 将原有请求中的header，数据转移至新的请求中
            if (headerNames != null) {
                while(headerNames.hasMoreElements()) {
                    name = (String)headerNames.nextElement();
                    String values = request.getHeader(name);
                    if (!name.equalsIgnoreCase("content-length")) {
                        requestTemplate.header(name, new String[]{values});
                    }
                }
            }

        }


        String corpId = SecurityUtils.getCorpId();
        if (StringUtils.isNotEmpty(corpId)) {
            String method = requestTemplate.method();
            if("GET".equals(method)){
                Collection<String> corpidParm = requestTemplate.queries().get("corpid");
                if(CollectionUtil.isEmpty(corpidParm)){
                    requestTemplate.query("corpid", corpId);
                }
            }
            if (requestTemplate.body() != null) {
                int length = requestTemplate.body().length;
                boolean hasFormData = requestTemplate.headers().get("Content-Type").stream()
                        .anyMatch(h -> h.contains("form-data"));
                if (0 < length && !hasFormData) {
                    String jsonBody = new String(requestTemplate.body());
                    if (!StringUtils.isEmpty(jsonBody)) {
                        JSONObject parse = JSONObject.parseObject(jsonBody);
                        parse.put("corpid", corpId);
                        // 替换请求体
                        byte[] bodyData = parse.toJSONString().getBytes(Util.UTF_8);
                        requestTemplate.body(bodyData, Util.UTF_8);
                    }
                }
            }
        } else {
            log.warn("url:{}  corpId 为null", requestTemplate.url());
        }
    }

    /**
     * 获取请求token
     */
    private String getToken(String token) {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }


}