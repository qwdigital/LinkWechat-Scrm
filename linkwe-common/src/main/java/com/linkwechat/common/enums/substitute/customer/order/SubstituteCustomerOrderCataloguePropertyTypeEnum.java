package com.linkwechat.common.enums.substitute.customer.order;

/**
 * 代客下单字段类型枚举
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/08/03 9:45
 */
public enum SubstituteCustomerOrderCataloguePropertyTypeEnum {
    SINGLE_LINE_TEXT(0, "单行文本"),
    MULTI_LINE_TEXT(1, "多行文本"),
    OPTIONS(2, "选项"),
    DATE_TIME(3, "日期时间"),
    DEPT(0, "选择部门"),
    USER(0, "选择员工"),
    NUMBER(0, "数字"),
    ATTACHMENT(0, "附件");


    private Integer code;

    private String type;


    SubstituteCustomerOrderCataloguePropertyTypeEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
