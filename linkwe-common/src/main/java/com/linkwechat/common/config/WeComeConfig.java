package com.linkwechat.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: Robin
 * @Description: 企业微信配置文件相关
 * @Date: create in 2020/9/3 0003 23:25
 */
@Component
@ConfigurationProperties(prefix = "wecome")
@Data
public class WeComeConfig {
    /** 企业微信后台地址 */
    private String serverUrl;

    /** 企业微信后台地址前缀 */
    private String weComePrefix;
}
