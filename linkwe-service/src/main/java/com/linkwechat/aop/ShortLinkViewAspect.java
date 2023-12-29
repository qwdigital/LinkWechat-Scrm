package com.linkwechat.aop;

import cn.hutool.core.util.StrUtil;
import com.linkwechat.common.annotation.ShortLinkView;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
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
import java.lang.reflect.Method;
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

            if(StringUtils.isEmpty(shortUrl) || Objects.equals("undefined",shortUrl)){
                return proceed;
            }

            String shortPromotionId = null;
            if(object.length >= 4){
                shortPromotionId = (String) object[3];
            }

            log.info("shortLinkView shortUrl：{}， 请求返回状态：RESPONSE : {} ", shortUrl, Objects.nonNull(resp) ? resp.getStatus() : "");
            if (HttpServletResponse.SC_OK != resp.getStatus()) {
                return proceed;
            }

            String prefix = getShortLinkViewInfo(joinPoint);

            //为了不改变之前的逻辑，以防万一
            if(prefix.contains("t:")){
                // 缓存 PV
                redisService.increment(WeConstans.WE_SHORT_LINK_KEY + WeConstans.PV + shortUrl);
                // 缓存 UV
                redisService.hyperLogLogAdd(WeConstans.WE_SHORT_LINK_KEY + WeConstans.UV + shortUrl, IpUtils.getIpAddr(servletRequest()));

                //短链推广统计
                if (StrUtil.isNotBlank(shortPromotionId)) {
                    // 缓存 PV
                    redisService.increment(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.PV + shortPromotionId);
                    // 缓存 UV
                    redisService.hyperLogLogAdd(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.UV + shortPromotionId, IpUtils.getIpAddr(servletRequest()));
                }
            }else {
                // 缓存 PV
                redisService.increment(WeConstans.WE_SHORT_LINK_COMMON_KEY + WeConstans.PV + prefix +shortUrl);
                // 缓存 UV
                redisService.hyperLogLogAdd(WeConstans.WE_SHORT_LINK_COMMON_KEY + WeConstans.UV + prefix + shortUrl, IpUtils.getIpAddr(servletRequest()));
            }

        } catch (Exception e) {
            log.error("shortLinkViewAspect 切面异常：", e);
        }
        return proceed;
    }


    /**
     * 获取注解信息
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    public String getShortLinkViewInfo(JoinPoint joinPoint) throws Exception {
        // 获取切入点的目标类
        String targetName = joinPoint.getTarget().getClass().getName();
        Class<?> targetClass = Class.forName(targetName);
        // 获取切入方法名
        String methodName = joinPoint.getSignature().getName();
        // 获取切入方法参数
        Object[] arguments = joinPoint.getArgs();
        // 获取目标类的所有方法
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            // 方法名相同、包含目标注解、方法参数个数相同（避免有重载）
            if (method.getName().equals(methodName) && method.isAnnotationPresent(ShortLinkView.class)
                    && method.getParameterTypes().length == arguments.length) {
                return method.getAnnotation(ShortLinkView.class).prefix();
            }
        }
        return "";
    }
}
