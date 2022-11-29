package com.linkwechat.common.enums;

import java.util.Objects;

/**
 * 商品订单状态枚举
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/23 10:39
 */
public enum ProductOrderStateEnum {

    FINISH(1, "已完成"),
    FINISH_BUT_REFUND(3, "已完成有退款");


    private Integer code;
    private String msg;

    ProductOrderStateEnum(Integer code, String msg) {
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

    public static ProductOrderStateEnum of(Integer code) {
        Objects.requireNonNull(code, "商品订单状态枚举类型不允许为空");
        for (ProductOrderStateEnum productOrderStateEnum : ProductOrderStateEnum.values()) {
            if (productOrderStateEnum.getCode().equals(code)) {
                return productOrderStateEnum;
            }
        }
        throw new IllegalArgumentException(String.format("未识别的商品订单状态枚举类型值[%s]", code));
    }

}
