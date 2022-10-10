package com.linkwechat.domain.wecom.vo.third.auth;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 企业微信推广返回结果
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeExtensionRegisterVo  extends WeResultVo {

    /**
     * 注册码，只能消费一次。在访问注册链接时消费。最长为512个字节
     */
    private String register_code;

    /**
     * register_code有效期，生成链接需要在有效期内点击跳转
     */
    private Long expires_in;

}