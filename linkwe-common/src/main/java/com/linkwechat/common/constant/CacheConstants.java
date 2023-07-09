package com.linkwechat.common.constant;

/**
 * 缓存的key 常量
 *
 * @author ruoyi
 */
public class CacheConstants {
    /**
     * 缓存有效期，默认720（分钟）
     */
    public final static Integer EXPIRATION = 720;

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public final static Integer REFRESH_TIME = 120;

    /**
     * 权限缓存前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /** 字典管理 cache key */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**路由key*/
    public static final String GATEWAY_ROUTES="gateway_dynamic_routes";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";
}
