package com.linkwechat.wecom.annotation;


import java.lang.annotation.*;

/**
 * 客户轨迹记录相关注解
 */
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomerTrajectoryRecord {
    int trajectoryType();
}
