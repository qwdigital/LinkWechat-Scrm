package com.linkwechat.domain.product.analyze.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品分析数据报表
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/22 18:52
 */
@Data
public class WeProductOrderDataReportVo {

    /**
     * 日期
     */
    @ExcelProperty(value = "日期", index = 0)
    private String dateStr;

    /**
     * 订单总数
     */
    @ExcelProperty(value = "订单总数", index = 1)
    private Integer orderNum;

    /**
     * 订单总额（元）
     */
    @ExcelProperty(value = "订单总额（元）", index = 2)
    private BigDecimal totalFee;

    /**
     * 退款总额（元）
     */
    @ExcelProperty(value = "退款总额（元）", index = 3)
    private BigDecimal refundFee;

    /**
     * 净收入（元）
     */
    @ExcelProperty(value = "净收入（元）", index = 4)
    private BigDecimal netIncome;
}
