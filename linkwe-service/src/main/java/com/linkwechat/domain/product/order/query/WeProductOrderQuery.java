package com.linkwechat.domain.product.order.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品订单查询
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/21 18:26
 */
@ApiModel
@Data
public class WeProductOrderQuery {

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("客户名称")
    private String externalName;

    @ApiModelProperty("收款员工")
    private String weUserId;

    @ApiModelProperty("交易状态")
    private Integer orderState;

    @ApiModelProperty("退款状态")
    private Integer refundState;

    @ApiModelProperty("交易开始时间")
    private String beginTime;

    @ApiModelProperty("交易结束时间")
    private String endTime;

}
