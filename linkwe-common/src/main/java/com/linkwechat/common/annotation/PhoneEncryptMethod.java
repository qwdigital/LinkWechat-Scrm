package com.linkwechat.common.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 手机号加密方法注解
 * @author: sxw
 * @date: 2022/9/15 16:47
 **/
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface PhoneEncryptMethod {
}
