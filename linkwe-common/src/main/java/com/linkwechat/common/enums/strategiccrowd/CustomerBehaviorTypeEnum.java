package com.linkwechat.common.enums.strategiccrowd;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户行为 发生类型
 */
@Getter
public enum CustomerBehaviorTypeEnum implements StrategicBaseEnum {

    /**
     * 发生类型
     */
    NOT_HAPPEN(1, "未发生过"),
    ONE_HAPPEN(2, "发生过一次"),
    TWO_HAPPEN(3, "发生过两次"),
    TREE_HAPPEN(4, "发生过三次"),
    TREE_MORE_HAPPEN(5, "发生过三次以上"),
    ;

    Integer code;

    String value;


    CustomerBehaviorTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static CustomerBehaviorTypeEnum parseEnum(Integer code) {
        CustomerBehaviorTypeEnum[] customerBehaviorTypeEnums = CustomerBehaviorTypeEnum.values();
        for (CustomerBehaviorTypeEnum customerBehaviorTypeEnum : customerBehaviorTypeEnums) {
            if (customerBehaviorTypeEnum.getCode() == code) {
                return customerBehaviorTypeEnum;
            }
        }
        return null;
    }


    public static List<Map<String, Object>> getType() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CustomerBehaviorTypeEnum customerBehaviorTypeEnum : values()) {
            Map<String, Object> energyTypeMap = new HashMap<>(16);
            energyTypeMap.put("code", customerBehaviorTypeEnum.getCode());
            energyTypeMap.put("value", customerBehaviorTypeEnum.getValue());
            list.add(energyTypeMap);
        }
        return list;
    }
}
