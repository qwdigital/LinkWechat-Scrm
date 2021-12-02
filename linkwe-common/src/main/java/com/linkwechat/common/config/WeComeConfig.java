package com.linkwechat.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

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

    /** 企业微信端无需token的url */
    private String[] noAccessTokenUrl;

    /** 需要使用外部联系人token的url*/
    private String[] needContactTokenUrl;

    /** 文件上传url*/
    private String[] fileUplodUrl;

    /** 需要供应商token的url*/
    private String[] needProviderTokenUrl;

    /** 会话存档所需token 的url */
    private String[] needChatTokenUrl;

    /** 第三方自建应用得url*/
    private String[] thirdAppUrl;

    /**多租户相关表*/
    private String[] needTenant=new String[]{};

    /**
     * 需要进行重试的企业微信请求错误码
     */
    private List<Integer> weNeedRetryErrorCodes;


}
