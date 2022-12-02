package com.linkwechat.domain.product.analyze.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品分析统计
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/22 15:47
 */
@Data
public class WeProductAnalyzeStatisticsVo {

    /**
     * 订单总数
     */
    private Integer orderNum;

    /**
     * 订单总额（元）
     */
    private BigDecimal totalFee;

    /**
     * 退款总额（元）
     */
    private BigDecimal refundFee;

    /**
     * 净收入（元）
     */
    private BigDecimal netIncome;

    /**
     * 今日订单总数
     */
    private Integer todayOrderNum;

    /**
     * 较昨日的订单总数
     */
    private Integer orderNumComparedToYes;

    /**
     * 今日订单总额（元）
     */
    private BigDecimal todayTotalFee;

    /**
     * 较昨日的订单总额（元）
     */
    private BigDecimal totalFeeComparedToYes;

    /**
     * 今日退款总额（元）
     */
    private BigDecimal todayRefundFee;

    /**
     * 较昨日的退款总额（元）
     */
    private BigDecimal refundFeeComparedToYes;

    /**
     * 今日净收入（元）
     */
    private BigDecimal todayNetIncome;

    /**
     * 较昨日的净收入（元）
     */
    private BigDecimal netIncomeComparedToYes;


}
