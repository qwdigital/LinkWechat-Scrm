package com.linkwechat.common.constant;

/**
 * 站点统计常量
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/14 10:28
 */
public class SiteStatsConstants {

    /**
     * PV 的 redis key（String 结构）
     */
    public static final String PREFIX_KEY_PV = "SITE_STAS:FORM_PV:{}:{}";

    /**
     * UV 的 redis key（String 结构）
     */
    public static final String PREFIX_KEY_UV = "SITE_STAS:FORM_UV:{}:{}";

    /**
     * IP 的 redis key（Set 结构，保存所有 IP）
     */
    public static final String PREFIX_KEY_IP = "SITE_STAS:FORM_IP:{}:{}";

}
