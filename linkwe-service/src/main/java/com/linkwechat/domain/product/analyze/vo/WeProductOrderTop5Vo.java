package com.linkwechat.domain.product.analyze.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品订单TOP5
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/22 18:13
 */
@Data
public class WeProductOrderTop5Vo {

    /**
     * 商品Id
     */
    private Long productId;

    /**
     * 商品封面
     */
    private String picture;

    /**
     * 商品描述
     */
    private String productDesc;

    /**
     * 订单总数
     */
    private Integer orderNum;

    /**
     * 订单总金额（元）
     */
    private BigDecimal totalFee;

    /**
     * 净收入
     */
    private BigDecimal netIncome;
}
