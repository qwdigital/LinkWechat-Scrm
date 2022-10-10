package com.linkwechat.common.core.domain.model;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录微信用户身份权限
 * 
 * @author danmo
 */
@Data
public class WxLoginUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户的唯一标识")
    private String openId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户的性别，值为1时是男性，值为2时是女性，值为0时是未知")
    private Integer sex;

    @ApiModelProperty("用户个人资料填写的省份")
    private String province;

    @ApiModelProperty("普通用户个人资料填写的城市")
    private String city;

    @ApiModelProperty("国家，如中国为CN")
    private String country;

    @ApiModelProperty("用户头像")
    private String headImgUrl;

    @ApiModelProperty("用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）")
    private JSONArray privilege;

    @ApiModelProperty("只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。")
    private String unionId;

    @ApiModelProperty("企微用户ID")
    private String externalUserId;

    /** 登录时间 */
    private Long loginTime;

    /** 过期时间 */
    private Long expireTime;

    /** 登录IP地址 */
    private String ipaddr;

    /** 用户唯一标识 */
    private String token;
}
