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
public enum RelationEnum implements StrategicBaseEnum {

    /**
     * 等于
     */
    EQUAL(1, "等于"),
    NOT_EQUAL(2, "不等于"),
    MORE_THAN(3, "大于"),
    GREATER_EQUAL(4, "大于等于"),
    LESS_THAN(5, "小于"),
    LESS_EQUAL(6, "小于等于"),
    INTERVAL(7, "区间"),
    NULL(8, "为空"),
    NOT_NULL(9, "不为空"),
    INCLUDE(10, "包含"),
    NOT_INCLUDE(11, "不包含"),

    ;

    Integer code;

    String value;


    RelationEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static RelationEnum parseEnum(Integer code) {
        RelationEnum[] relationEnums = RelationEnum.values();
        for (RelationEnum relationEnum : relationEnums) {
            if (relationEnum.getCode() == code) {
                return relationEnum;
            }
        }
        return null;
    }


    public static List<Map<String, Object>> getType() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RelationEnum corpAddStateEnum : values()) {
            Map<String, Object> energyTypeMap = new HashMap<>(16);
            energyTypeMap.put("code", corpAddStateEnum.getCode());
            energyTypeMap.put("value", corpAddStateEnum.getValue());
            list.add(energyTypeMap);
        }
        return list;
    }
}
