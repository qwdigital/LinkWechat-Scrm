package com.linkwechat.domain.substitute.customer.order.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 代客下单-订单
 * </p>
 *
 * @author WangYX
 * @since 2023-08-03
 */
@Data
public class WeSubstituteCustomerOrderRequest {

    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private String orderStatus;

    /**
     * 外部联系人的userid
     */
    @ApiModelProperty("外部联系人的userid")
    private String externalUserid;

    /**
     * 员工Id
     */
    @ApiModelProperty("员工Id")
    private String userId;
}
