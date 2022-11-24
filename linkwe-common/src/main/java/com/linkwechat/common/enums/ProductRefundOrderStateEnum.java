package com.linkwechat.common.enums;

import java.util.Objects;

/**
 * 商品退款订单状态枚举
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/23 10:39
 */
public enum ProductRefundOrderStateEnum {

    APPLIED(1, "已申请退款"),
    BEING_PROCESSED(2, "退款处理中"),
    REFUND_SUCCESS(3, "退款成功"),
    REFUND_CLOSE(4, "退款关闭"),
    REFUND_EXCEPTION(5, "退款异常"),
    APPROVAL(6, "审批中"),
    APPROVAL_FAIL(7, "审批失败"),
    APPROVAL_CANCEL(8, "审批取消"),
    ;


    private Integer code;
    private String msg;

    ProductRefundOrderStateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static ProductRefundOrderStateEnum of(Integer code) {
        Objects.requireNonNull(code, "商品退款订单状态枚举类型不允许为空");
        for (ProductRefundOrderStateEnum productRefundOrderStateEnum : ProductRefundOrderStateEnum.values()) {
            if (productRefundOrderStateEnum.getCode().equals(code)) {
                return productRefundOrderStateEnum;
            }
        }
        throw new IllegalArgumentException(String.format("未识别的商品退款订单状态枚举类型值[%s]", code));
    }

}
