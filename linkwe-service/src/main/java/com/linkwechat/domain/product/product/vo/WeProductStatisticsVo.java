package com.linkwechat.domain.product.product.vo;

import lombok.Data;

/**
 * 商品统计
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/22 11:19
 */
@Data
public class WeProductStatisticsVo {

    /**
     * 订单总数
     */
    private Integer orderNum;

    /**
     * 订单总金额（元）
     */
    private String orderFee;

    /**
     * 退款总金额
     */
    private String refundFee;

    /**
     * 净收入（元）
     */
    private String netIncome;


}
