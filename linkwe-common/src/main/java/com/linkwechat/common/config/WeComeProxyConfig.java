package com.linkwechat.common.config;

import lombok.Data;

@Data
public class WeComeProxyConfig {

    //是否启动返代理,默认是不启用的
    private boolean startProxy=false;

    //代理服务器地址
    private String proxyIp;

    //代理服务器端口
    private int proxyPort;

    //代理用户名
    private String proxyUserName;

    //代理密码
    private String proxyPassword;

}
