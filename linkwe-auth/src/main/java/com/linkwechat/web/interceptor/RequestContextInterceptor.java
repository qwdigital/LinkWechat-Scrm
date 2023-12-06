package com.linkwechat.web.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.SecurityConstants;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.domain.model.WxLoginUser;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){


        boolean isWx=false;

        String loginType = ServletUtils.getHeader(request, SecurityConstants.LOGIN_TYPE);


        String url = request.getRequestURI();
        if(StringUtils.isNotEmpty(url)){
            // 如果在微信接口集合下则需要微信授权的token才可以访问
            if ("/system/user/getWxInfo".equals(url)) {
                isWx=true;
                if(StringUtils.isNotEmpty(loginType) && ObjectUtil.notEqual("LinkWeChatWXAPI",loginType)){
                    throw new WeComException("token不合法");
                }

            }else{ //通过pc获取的token接口

                SecurityContextHolder.setCorpId(ServletUtils.getHeader(request, SecurityConstants.CORP_ID));
                SecurityContextHolder.setCorpName(ServletUtils.getHeader(request, SecurityConstants.CORP_NAME));
                SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants.USER_ID));
                SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants.USER_NAME));
                SecurityContextHolder.setUserType(ServletUtils.getHeader(request, SecurityConstants.USER_TYPE));
                SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.USER_KEY));


//                if(StringUtils.isNotEmpty(loginType)){
//                    if(ObjectUtil.notEqual("LinkWeChatAPI",loginType)||
//                            SecurityConstants.IS_FEGIN.equals(
//                                    ServletUtils.getHeader(request, SecurityConstants.IS_FEGIN)
//                            )){
//                        throw new WeComException("token不合法");
//                    }
//                }


            }
        }



        String loginUserStr = ServletUtils.getHeader(request, SecurityConstants.LOGIN_USER);
        if(StringUtils.isNotEmpty(loginUserStr)){
            SecurityContextHolder.set(SecurityConstants.Details.LOGIN_USER.getCode(), JSONObject.parseObject(loginUserStr,
                    isWx?WxLoginUser.class:LoginUser.class));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.remove();
    }
}