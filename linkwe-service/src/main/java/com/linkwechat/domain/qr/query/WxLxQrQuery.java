package com.linkwechat.domain.qr.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author danmo
 * @description 拉新活码新增入参
 * @date 2023/03/07 13:49
 **/
@ApiModel
@Data
public class WxLxQrQuery {

    @ApiModelProperty("活码Id")
    @NotNull(message = "活码名称不能为空")
    private Long qrId;

    @ApiModelProperty("红包订单ID")
    private String orderNo;
}
