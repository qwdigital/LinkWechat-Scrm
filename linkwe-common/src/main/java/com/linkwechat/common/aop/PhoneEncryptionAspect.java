package com.linkwechat.common.aop;

import cn.hutool.core.util.DesensitizedUtil;
import com.linkwechat.common.annotation.PhoneEncryptField;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;

/**
 * 手机号加密切面
 *
 * @author danmo
 * @date 2022年09月15日 14:26
 */
@Slf4j
@Aspect
@Component
public class PhoneEncryptionAspect {

    private Object responseObj;

    @Pointcut("@annotation(com.linkwechat.common.annotation.PhoneEncryptMethod)")
    public void annotationPointCut() {
    }

    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Exception {
        Object responseObj = new Object();
        try {
            Object requestObj = joinPoint.getArgs()[0];
            responseObj = joinPoint.proceed();
            handleEncrypt(responseObj);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            log.error("PhoneEncryptionAspect处理出现异常", e);
            throw new RuntimeException("手机号加解密服务异常");
        } catch (Throwable e) {
            log.error("PhoneEncryptionAspect处理出现异常", e);
            throw new RuntimeException("手机号加解密服务异常");
        }
        return responseObj;
    }

    /**
     * 处理加密
     *
     * @param responseObj
     */
    private void handleEncrypt(Object responseObj) throws Exception {
        if (Objects.isNull(responseObj)) {
            return;
        }
        //获取传入参数的字段list
        if (responseObj instanceof Collection) {
            ((Collection<?>) responseObj).forEach(item -> {
                Field[] dFields = item.getClass().getDeclaredFields();
                extracted(item, dFields);
            });
        } else {
            Field[] dFields = responseObj.getClass().getDeclaredFields();
            extracted(responseObj, dFields);
        }
    }

    private void extracted(Object item, Field[] dFields) {
        for (Field field : dFields) {
            //获取参数中加过注解的字段
            boolean hasSecureField = field.isAnnotationPresent(PhoneEncryptField.class);
            //判断有没有加过注解的字段
            if (hasSecureField) {
                field.setAccessible(true);
                String plaintextValue;
                String encryptValue = null;
                try {
                    if (field.get(item) != null) {
                        plaintextValue = (String) field.get(item);
                        //如果有，进行加密操作，然后替换成加密之后的值
                        encryptValue = DesensitizedUtil.mobilePhone(plaintextValue);
                        field.set(item, encryptValue);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
