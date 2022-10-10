package com.linkwechat.domain.wecom.vo.weixin;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description
 * @date 2021/4/5 16:01
 **/
@ApiModel
@Data
public class WxTokenVo extends WeResultVo {

    @ApiModelProperty("网页授权接口调用凭证token")
    private String accessToken;

    @ApiModelProperty("超时时间，单位（秒）")
    private Long expiresIn;

    @ApiModelProperty("用户刷新access_token")
    private String refreshToken;

    @ApiModelProperty("用户唯一标识")
    private String openId;

    @ApiModelProperty("用户授权的作用域，使用逗号（,）分隔")
    private String scope;
}
