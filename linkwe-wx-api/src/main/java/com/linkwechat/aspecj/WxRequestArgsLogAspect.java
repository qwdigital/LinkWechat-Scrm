package com.linkwechat.aspecj;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.corp.query.WeCorpAccountQuery;
import com.linkwechat.domain.corp.vo.WeCorpAccountVo;
import com.linkwechat.domain.wx.WxBaseQuery;
import com.linkwechat.service.IWeCorpAccountService;
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
public class WxRequestArgsLogAspect {
    private static final Logger log = LoggerFactory.getLogger(WxRequestArgsLogAspect.class);

    private final IWeCorpAccountService weCorpAccountService = SpringUtils.getBean(IWeCorpAccountService.class);

    // 配置织入点
    @Pointcut("execution(public * com.linkwechat.controller..*.*(..))) && !execution(com.other.controller.OtherController..*.*(..))")
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
        log.info("wecom 接口请求入参className:{},methodName:{},params:{}", className, methodName, JSONObject.toJSONString(args));
        for (Object arg : args) {
            if (arg instanceof WxBaseQuery) {
                WxBaseQuery query = (WxBaseQuery) arg;
                String corpId = query.getCorpId();
                WeCorpAccount accountInfo = weCorpAccountService.getCorpAccountByCorpId(corpId);
                if(accountInfo != null){
                    query.setCorpId(accountInfo.getCorpId());
                    SecurityContextHolder.setCorpId(accountInfo.getCorpId());
                }
            }
        }
    }
}
