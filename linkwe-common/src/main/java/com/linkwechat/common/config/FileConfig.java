package com.linkwechat.common.config;

import lombok.Data;

@Data
public class FileConfig {

    /**是否开启云存储*/
    private boolean startCosUpload=false;
    /**腾讯云存储相关配置*/
    private CosConfig cos;
    /**文件前缀*/
    private String imgUrlPrefix;

    /**云存储平台 腾讯云cos:tencentOss 阿里云oss:aliOss **/
    private String object;

   /**Aes加解密的key*/
    private String aesKey;
}
