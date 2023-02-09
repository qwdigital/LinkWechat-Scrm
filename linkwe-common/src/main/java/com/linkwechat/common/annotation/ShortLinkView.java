package com.linkwechat.common.annotation;

import java.lang.annotation.*;

/**
 * @author danmo
 * @date 2023年01月09日 14:07
 */

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface  ShortLinkView {

    /**
     * 缓存命名空间前缀
     */
    String prefix() default "";
}
