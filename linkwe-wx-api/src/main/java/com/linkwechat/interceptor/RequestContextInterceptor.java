package com.linkwechat.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.SecurityConstants;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.model.WxLoginUser;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 *
 * @author xueyi
 */
@Slf4j
@Component
public class RequestContextInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String loginType = ServletUtils.getHeader(request, SecurityConstants.LOGIN_TYPE);
        if(StringUtils.isNotEmpty(loginType)){
            if(ObjectUtil.notEqual("LinkWeChatWXAPI",loginType)||
                    SecurityConstants.IS_FEGIN.equals(
                            ServletUtils.getHeader(request, SecurityConstants.IS_FEGIN)
                    )){
                throw new WeComException("token不合法");
            }
        }
        String loginUserStr = ServletUtils.getHeader(request, SecurityConstants.LOGIN_USER);
        if(StringUtils.isNotEmpty(loginUserStr)){
            SecurityContextHolder.set(SecurityConstants.Details.LOGIN_USER.getCode(), JSONObject.parseObject(loginUserStr, WxLoginUser.class));
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        SecurityContextHolder.remove();
    }
}