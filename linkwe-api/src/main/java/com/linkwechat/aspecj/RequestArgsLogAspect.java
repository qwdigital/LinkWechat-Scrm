package com.linkwechat.aspecj;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.utils.ip.IpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 请求入参日志记录处理
 *
 * @author ruoyi
 */
//@Aspect
//@Component
public class RequestArgsLogAspect {
    private static final Logger log = LoggerFactory.getLogger(RequestArgsLogAspect.class);


    // 配置织入点
    @Pointcut("execution(public * com.linkwechat.controller..*.*(..)))")
    public void logPointCut() {
    }

    /**
     * 处理请求前日志打印
     */
    @Before(value = "logPointCut()")
    public void doBefore(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ipAddr = IpUtils.getIpAddr(request);
        Object[] params = new Object[args.length];
        for (int i = 0; i< args.length;i++) {
            if (!(args[i] instanceof HttpServletRequest) && !(args[i] instanceof HttpServletResponse) && !(args[i] instanceof MultipartFile)) {
                params[i]=args[i];
            }
        }
        log.info("api 接口请求 url:{},HTTP:{},IP:{},params:{}", url, request.getMethod(), ipAddr, JSONObject.toJSONString(params));
    }
}
