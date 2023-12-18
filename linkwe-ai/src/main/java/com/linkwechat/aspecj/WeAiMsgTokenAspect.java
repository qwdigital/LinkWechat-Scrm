//package com.linkwechat.aspecj;
//
//import com.linkwechat.common.exception.wecom.WeComException;
//import com.linkwechat.service.IWeAiMsgService;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Aspect
//@Component
//public class WeAiMsgTokenAspect {
//
//    @Autowired
//    private IWeAiMsgService weAiMsgService;
//
//    @Value("${ai.token.num:10000}")
//    private int tokenTotalNum;
//
//
//    @Pointcut("@annotation(com.linkwechat.annotation.AiMsgAop)")
//    public void annotationPointCut() {
//    }
//
//    @Before(value = "annotationPointCut()")
//    public void tokenComputeHandle(JoinPoint joinPoint) throws Throwable {
//        Object[] args = joinPoint.getArgs();
//        if (args == null || args.length <= 0) {
//            return;
//        }
//        Integer todayToken = weAiMsgService.computeTodayToken();
//        if(todayToken != null && todayToken >= tokenTotalNum){
//            throw new WeComException("今天已超过配额限制");
//        }
//    }
//}
