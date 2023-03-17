package com.linkwechat.aop;

import cn.hutool.core.util.StrUtil;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author danmo
 * @date 2023年01月09日 14:09
 */
@Slf4j
@Aspect
@Component
public class ShortLinkViewAspect {

    @Autowired
    private RedisService redisService;

    /**
     * 获取当前的ServletRequest
     *
     * @return
     */
    protected HttpServletRequest servletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }


    @Pointcut("@annotation(com.linkwechat.common.annotation.ShortLinkView)")
    public void shortLinkViewAspect() {
    }


    @Around(value = "shortLinkViewAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();
        try {
            Object[] object = joinPoint.getArgs();
            HttpServletResponse resp = (HttpServletResponse) object[1];
            String shortUrl = (String) object[2];
            String shortPromotionId = (String) object[3];
            log.info("shortLinkView shortUrl：{}， 请求返回状态：RESPONSE : {} ", shortUrl, Objects.nonNull(resp) ? resp.getStatus() : "");
            if (HttpServletResponse.SC_OK != resp.getStatus()) {
                return proceed;
            }
            // 缓存 PV
            redisService.increment(WeConstans.WE_SHORT_LINK_KEY + WeConstans.PV + shortUrl);
            // 缓存 UV
            redisService.hyperLogLogAdd(WeConstans.WE_SHORT_LINK_KEY + WeConstans.UV + shortUrl, IpUtils.getIpAddr(servletRequest()));

            //短链推广统计
            if (StrUtil.isNotBlank(shortPromotionId)) {
                // 缓存 PV
                redisService.increment(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.PV + shortUrl);
                // 缓存 UV
                redisService.hyperLogLogAdd(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.UV + shortUrl, IpUtils.getIpAddr(servletRequest()));
            }
        } catch (Exception e) {
            log.error("shortLinkViewAspect 切面异常：", e);
        }
        return proceed;
    }
}
