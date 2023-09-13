package com.linkwechat.domain.substitute.customer.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 代客下单-订单
 * </p>
 *
 * @author WangYX
 * @since 2023-08-03
 */
@Data
public class WeSubstituteCustomerOrderVO {

    /**
     * 主键Id
     */
    @ApiModelProperty("主键Id")
    private Long id;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String productName;

    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    private String productUrl;

    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private String orderStatus;

    /**
     * 付款总价
     */
    @ApiModelProperty("付款总价")
    private BigDecimal totalPrice;

    /**
     * 购买人
     */
    @ApiModelProperty("购买人")
    private String purchaser;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime orderTime;


    /**
     * 商品数量
     */
    @ApiModelProperty("商品数量")
    private Integer amount;

}
