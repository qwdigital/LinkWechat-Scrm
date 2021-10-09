package com.linkwechat.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * @description: 客户轨迹记录AOP
 * @author: HaoN
 * @create: 2021-05-02 22:51
 **/
@Aspect
@Component
public class CustomerTrajectoryAop {

    @Pointcut("@annotation(com.linkwechat.wecom.annotation.CustomerTrajectoryRecord)")
    public void customerTrajectoryPointCut(){}


    /**
     * 记录轨迹
     * @param joinPoint
     */
    @Around("customerTrajectoryPointCut()")
    public void customerTrajectoryRecord(ProceedingJoinPoint joinPoint){

    }
}
