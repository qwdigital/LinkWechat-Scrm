package com.linkwechat.common.aop;

import com.alibaba.fastjson.JSON;
import com.linkwechat.common.annotation.RepeatSubmit;
import com.linkwechat.common.constant.CacheConstants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.filter.RepeatedlyRequestWrapper;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.http.HttpHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 防止表单重复提交
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/21 14:34
 */
@Aspect
@Component
public class RepeatSubmitAspect {


    public final String REPEAT_PARAMS = "repeatParams";

    public final String REPEAT_TIME = "repeatTime";

    @Resource
    private RedisService redisService;


    @Pointcut("@annotation(com.linkwechat.common.annotation.RepeatSubmit)")
    public void pointCut() {
    }


    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        MethodSignature sign = (MethodSignature) point.getSignature();
        Method method = sign.getMethod();
        RepeatSubmit repeatSubmit = method.getAnnotation(RepeatSubmit.class);

        boolean b = isRepeatSubmit(request, repeatSubmit);
        if (b) {
            return AjaxResult.error(repeatSubmit.message());
        }
        return point.proceed();
    }

    /**
     * 判断是否重复提交
     *
     * @param request
     * @param repeatSubmit
     * @return
     */
    public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit repeatSubmit) {
        String nowParams = "";
        if (request instanceof RepeatedlyRequestWrapper) {
            RepeatedlyRequestWrapper repeatedlyRequest = (RepeatedlyRequestWrapper) request;
            nowParams = HttpHelper.getBodyString(repeatedlyRequest);
        }

        // body参数为空，获取Parameter的数据
        if (StringUtils.isEmpty(nowParams)) {
            nowParams = JSON.toJSONString(request.getParameterMap());
        }
        Map<String, Object> nowDataMap = new HashMap<String, Object>();
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());

        // 请求地址（作为存放cache的key值）
        String url = request.getRequestURI();

        // 唯一值（没有消息头则使用请求地址）
        String submitKey = StringUtils.trimToEmpty(request.getHeader("Authorization"));

        // 唯一标识（指定key + url + 消息头）
        String cacheRepeatKey = CacheConstants.REPEAT_SUBMIT_KEY + url + submitKey;

        Object sessionObj = redisService.getCacheObject(cacheRepeatKey);
        if (sessionObj != null) {
            Map<String, Object> sessionMap = (Map<String, Object>) sessionObj;
            if (sessionMap.containsKey(url)) {
                Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(url);
                if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap, repeatSubmit.interval())) {
                    return true;
                }
            }
        }
        Map<String, Object> cacheMap = new HashMap<String, Object>();
        cacheMap.put(url, nowDataMap);
        redisService.setCacheObject(cacheRepeatKey, cacheMap, repeatSubmit.interval(), TimeUnit.MILLISECONDS);
        return false;
    }

    /**
     * 判断参数是否相同
     */
    private boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    /**
     * 判断两次间隔时间
     */
    private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap, int interval) {
        long time1 = (Long) nowMap.get(REPEAT_TIME);
        long time2 = (Long) preMap.get(REPEAT_TIME);
        if ((time1 - time2) < interval) {
            return true;
        }
        return false;
    }


}
