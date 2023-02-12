package com.linkwechat.domain.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @Description 微信请求基础入参
 * @date 2021/12/2 18:27
 **/
@Data
public class WxBaseQuery {

    @ApiModelProperty(value = "企业ID",hidden = true)
    public String corpId;

    @ApiModelProperty(value = "接口凭证")
    public String accessToken;
}
