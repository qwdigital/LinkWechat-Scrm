package com.linkwechat.common.enums.strategiccrowd;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户行为时间
 */
@Getter
public enum CustomerBehaviorTimeEnum implements StrategicBaseEnum {

    /**
     * 今日
     */
    TODAY(1, "今日"),
    YESTERDAY(2, "昨日"),
    LAST_THREE_DAYS(3, "近三天"),
    LAST_SEVEN_DAYS(4, "近七天"),
    CUSTOMIZE(5, "自定义"),
    INTERVAL(6, "区间"),
    ;

    Integer code;

    String value;


    CustomerBehaviorTimeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static CustomerBehaviorTimeEnum parseEnum(Integer code) {
        CustomerBehaviorTimeEnum[] customerBehaviorTimeEnums = CustomerBehaviorTimeEnum.values();
        for (CustomerBehaviorTimeEnum customerBehaviorTimeEnum : customerBehaviorTimeEnums) {
            if (customerBehaviorTimeEnum.getCode() == code) {
                return customerBehaviorTimeEnum;
            }
        }
        return null;
    }


    public static List<Map<String, Object>> getType() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CustomerBehaviorTimeEnum corpAddStateEnum : values()) {
            Map<String, Object> energyTypeMap = new HashMap<>(16);
            energyTypeMap.put("code", corpAddStateEnum.getCode());
            energyTypeMap.put("value", corpAddStateEnum.getValue());
            list.add(energyTypeMap);
        }
        return list;
    }
}
