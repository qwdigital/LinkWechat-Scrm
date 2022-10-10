package com.linkwechat.common.config;

import lombok.Data;

/**
 * 企业微信侧边栏相关配置
 */
@Data
public class WeSideBarConfig {

    private Material material;


    private CustomerPortrait customerPortrait;


    private WordGroup wordGroup;


    private RedEnvelopes redEnvelopes;


    //素材相关
    @Data
    public static class Material{

        //素材名称
        private String materialName;
        //素材地址
        private String materialUrl;
    }

    //客户画像
    @Data
    public static class CustomerPortrait{

        //客户画像名称
        private String customerPortraitName;

        //客户画像url
        private String customerPortraitUrl;

    }


    //词群
    @Data
    public static class WordGroup{
        //词群名称
        private String wordGroupName;
        //词群url
        private String wordGroupUrl;
    }

    //红包
    @Data
    public static class RedEnvelopes{
        //红包名
        private String redEnvelopesName;
        //红包url
        private String redEnvelopesUrl;
    }
}
