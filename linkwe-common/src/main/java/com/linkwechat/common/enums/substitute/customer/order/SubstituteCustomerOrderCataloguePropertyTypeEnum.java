package com.linkwechat.common.enums.substitute.customer.order;

import cn.hutool.core.bean.BeanUtil;

import java.util.Arrays;
import java.util.Optional;

/**
 * 代客下单字段类型枚举
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/08/03 9:45
 */
public enum SubstituteCustomerOrderCataloguePropertyTypeEnum {

    /**
     * 单行文本
     */
    SINGLE_LINE_TEXT(0, "单行文本"),
    /**
     * 多行文本
     */
    MULTI_LINE_TEXT(1, "多行文本"),
    /**
     * 选项
     */
    OPTIONS(2, "选项"),
    /**
     * 日期时间
     */
    DATE_TIME(3, "日期时间"),
    /**
     * 选择部门
     */
    DEPT(4, "选择部门"),
    /**
     * 选择员工
     */
    USER(5, "选择员工"),
    /**
     * 数字
     */
    NUMBER(6, "数字"),
    /**
     * 附件
     */
    ATTACHMENT(7, "附件");


    /**
     * 编码
     */
    private final Integer code;

    /**
     * 类型
     */
    private final String type;


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


    public static String byCode(Integer code) {
        SubstituteCustomerOrderCataloguePropertyTypeEnum[] values = SubstituteCustomerOrderCataloguePropertyTypeEnum.values();
        Optional<SubstituteCustomerOrderCataloguePropertyTypeEnum> first = Arrays.stream(values).filter(i -> i.getCode().equals(code)).findFirst();
        SubstituteCustomerOrderCataloguePropertyTypeEnum item = first.orElse(null);
        return BeanUtil.isNotEmpty(item) ? item.getType() : null;
    }
}
