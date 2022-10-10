package com.linkwechat.common.annotation;

import java.lang.annotation.*;

/**
 * @author danmo
 * @description
 * @date 2022/5/10 17:47
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(DataScope.class)
public @interface DataColumn {

    /**
     * 主表别名
     * @return
     */
    String alias() default "";

    /**
     * 字段名称 create_by
     * @return
     */
    String name();

    /**
     * 用户id user_id 或 we_user_id
     * @return
     */
    String userid();

}
