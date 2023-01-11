package com.linkwechat.domain.shortlink.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author danmo
 * @description 短链新增出参
 * @date 2022/12/19 13:49
 **/
@ApiModel
@Data
public class WeShortLinkAddVo {

    @ApiModelProperty(value = "短链地址")
    private String shortUrl;

    @ApiModelProperty(value = "二维码地址")
    private String qrCode;
}
