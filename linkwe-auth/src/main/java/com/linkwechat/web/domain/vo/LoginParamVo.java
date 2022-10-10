package com.linkwechat.web.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 登陆所需相关配置
 */
@Data
@Builder
public class LoginParamVo {
    //加入企业的二维码
    private String joinCorpQr;
    //登陆二维码
    private String loginQr;
 }
