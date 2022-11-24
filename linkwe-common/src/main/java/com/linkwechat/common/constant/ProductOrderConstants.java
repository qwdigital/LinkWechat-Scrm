package com.linkwechat.common.constant;

/**
 * 商品订单常量
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/22 17:22
 */
public class ProductOrderConstants {

    /**
     * Redis当天的订单总数-key，单位：分
     */
    public static final String PRODUCT_ANALYZE_ORDER_NUMBER = "PRODUCT_ANALYZE_ORDER_NUMBER";
    /**
     * Redis当天的订单总额-key，单位：分
     */
    public static final String PRODUCT_ANALYZE_ORDER_TOTAL_FEE = "PRODUCT_ANALYZE_ORDER_TOTAL_FEE";
    /**
     * Redis当天的退款总额-key，单位：分
     */
    public static final String PRODUCT_ANALYZE_ORDER_REFUND_FEE = "PRODUCT_ANALYZE_ORDER_REFUND_FEE";


}
