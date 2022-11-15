package com.linkwechat.common.enums.strategiccrowd;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户属性
 */
@Getter
public enum CustomerAttributesEnum implements StrategicBaseEnum {

    /**
     * 名称
     */
    NAME(1, "名称"),
    SEX(2, "性别"),
    AGE(3, "年龄"),
    PHONE(4, "手机号"),
    BIRTHDAY(5, "生日"),
    EMAIL(6, "邮箱"),
    AREA(7, "所在省市区"),
    QQ(8, "QQ"),
    PROFESSION(9, "职业"),
    COMPANY(10, "公司"),
    CUSTOMER_TYPE(11, "客户类型"),
    FOLLOW_STATUS(12, "跟进状态"),
    FOLLOW_DEPART(13, "跟进部门"),
    FOLLOW_USER(14, "跟进员工"),
    ADD_TIME(15, "首次添加时间"),
    LOST_TIME(16, "流失时间"),
    LAST_ACTIVE_TIME(17, "最后活跃时间"),

    ;

    Integer code;

    String value;


    CustomerAttributesEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static CustomerAttributesEnum parseEnum(Integer code) {
        CustomerAttributesEnum[] customerAttributesEnums = CustomerAttributesEnum.values();
        for (CustomerAttributesEnum customerAttributesEnum : customerAttributesEnums) {
            if (customerAttributesEnum.getCode() == code) {
                return customerAttributesEnum;
            }
        }
        return null;
    }


    public static List<Map<String, Object>> getType() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CustomerAttributesEnum corpAddStateEnum : values()) {
            Map<String, Object> energyTypeMap = new HashMap<>(16);
            energyTypeMap.put("code", corpAddStateEnum.getCode());
            energyTypeMap.put("value", corpAddStateEnum.getValue());
            list.add(energyTypeMap);
        }
        return list;
    }
}
