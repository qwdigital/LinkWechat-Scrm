package com.linkwechat.domain.product.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 商品订单
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/21 18:26
 */
@ApiModel
@Data
public class WeProductOrderPayInfoVo {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "商户单号")
    private String mchNo;

    @ApiModelProperty(value = "订单状态（1已完成，2已完成有退款）")
    private Integer orderState;

    @ApiModelProperty(value = "付款总金额")
    private String totalFee;

    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    @ApiModelProperty(value = "交易时间")
    private Date payTime;

    @ApiModelProperty(value = "产品Id")
    private Long productId;

    @ApiModelProperty(value = "购买数量")
    private Integer productNum;

}
