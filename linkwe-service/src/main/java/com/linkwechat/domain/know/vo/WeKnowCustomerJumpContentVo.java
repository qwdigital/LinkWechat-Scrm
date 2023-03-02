package com.linkwechat.domain.know.vo;

import lombok.Data;

/**
 * 老客跳转的内容
 */
@Data
public class WeKnowCustomerJumpContentVo {


    //跳转引导语
    private String jumpGuide;
    //跳转url(链接地址或小程序地址)
    private String jumpUrl;
    //跳转应用标题(小程序标题)
    private String jumpTitle;
    //其他内容(比如小程序id)
    private String jumpOther;
}
