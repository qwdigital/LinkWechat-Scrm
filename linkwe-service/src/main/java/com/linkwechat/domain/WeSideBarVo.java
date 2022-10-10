package com.linkwechat.domain;

import lombok.Builder;
import lombok.Data;

/**
 * 侧边栏相关配置
 */
@Data
@Builder
public class WeSideBarVo {
    //素材名称
    private String materialName;
    //素材地址
    private String materialUrl;

    //客户画像名称
    private String customerPortraitName;

    //客户画像url
    private String customerPortraitUrl;

    //词群名称
    private String wordGroupName;
    //词群url
    private String wordGroupUrl;

    //红包名
    private String redEnvelopesName;
    //红包url
    private String redEnvelopesUrl;
}
