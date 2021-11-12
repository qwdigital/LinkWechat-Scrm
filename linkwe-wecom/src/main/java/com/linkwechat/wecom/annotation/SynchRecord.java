package com.linkwechat.wecom.annotation;

import java.lang.annotation.*;


/**
 * 同步记录注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SynchRecord {

    int synchType() default 1; //1：默认客户模块;2:员工与部门;

}
