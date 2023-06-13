package com.linkwechat.common.context;


import com.alibaba.ttl.TransmittableThreadLocal;
import com.linkwechat.common.constant.SecurityConstants;
import com.linkwechat.common.core.text.Convert;
import com.linkwechat.common.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取当前线程变量中的 企业Id | 企业账号 | 租户类型 | 用户id | 用户账号 | 用户类型 | Token等信息
 * 注意： 必须在网关通过请求头的方法传入，同时在HeaderInterceptor拦截器设置值。 否则这里无法获取
 *
 * @author danmo
 */
public class SecurityContextHolder {

    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StringUtils.EMPTY : value);
    }

    public static void remove(String key) {
        Map<String, Object> map = getLocalMap();
        map.remove(key);
    }

    public static String get(String key) {
        Map<String, Object> map = getLocalMap();
        return Convert.toStr(map.getOrDefault(key, StringUtils.EMPTY));
    }

    public static <T> T get(String key, Class<T> clazz) {
        Map<String, Object> map = getLocalMap();
        return StringUtils.cast(map.getOrDefault(key, null));
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<String, Object>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }



    public static void setLocalMap(Map<String, Object> threadLocalMap) {
        THREAD_LOCAL.set(threadLocalMap);
    }

    public static String getCorpId() {
        return Convert.toStr(get(SecurityConstants.Details.CORP_ID.getCode()));
    }

    public static void setCorpId(String corpId) {
        set(SecurityConstants.Details.CORP_ID.getCode(), corpId);
    }

    public static String getCorpName() {
        return get(SecurityConstants.Details.CORP_NAME.getCode());
    }

    public static void setCorpName(String corpName) {
        set(SecurityConstants.Details.CORP_NAME.getCode(), corpName);
    }

    public static Long getUserId() {
        return Convert.toLong(get(SecurityConstants.Details.USER_ID.getCode()), 0L);
    }

    public static void setUserId(String userId) {
        set(SecurityConstants.Details.USER_ID.getCode(), userId);
    }

    public static String getUserName() {
        return get(SecurityConstants.Details.USER_NAME.getCode());
    }

    public static void setUserName(String userName) {
        set(SecurityConstants.Details.USER_NAME.getCode(), userName);
    }

    public static String getUserType() {
        return get(SecurityConstants.Details.USER_TYPE.getCode());
    }

    public static void setUserType(String userType) {
        set(SecurityConstants.Details.USER_TYPE.getCode(), userType);
    }

    public static String getUserKey() {
        return get(SecurityConstants.Details.USER_KEY.getCode());
    }

    public static void setUserKey(String userKey) {
        set(SecurityConstants.Details.USER_KEY.getCode(), userKey);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static void setTenantId(Integer tenantId) {
        set(SecurityConstants.Details.TENANT_ID.getCode(), tenantId);
    }

    public static Integer getTenantId() {
        return Convert.toInt(get(SecurityConstants.Details.TENANT_ID.getCode()));
    }
}
