package com.linkwechat.domain.wecom.callback.third;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 授权通知
 * @date 2022/3/7 22:00
 **/
@ApiModel
@Data
public class WeThirdCreateAuthVo extends WeThirdBackBaseVo{

    @ApiModelProperty("临时授权码,最长为512字节。用于获取企业永久授权码。10分钟内有效")
    private String AuthCode;
}
