package com.linkwechat.aspecj;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 请求入参日志记录处理
 *
 * @author ruoyi
 */
@Aspect
@Component
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
        // 设置方法名称
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.info("api 接口请求 className:{},methodName:{},params:{}", className, methodName, JSONObject.toJSONString(args));
    }
}
