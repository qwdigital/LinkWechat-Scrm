package com.linkwechat.common.enums.strategicjourney;

import lombok.Getter;

/**
 * 执行方式
 */
@Getter
public enum JourneyTypeEnum {


    IMMEDIATELY(1, "立即执行"),
    TIMING(2, "定时执行"),
    REPEAT(3, "重复执行"),

    ;

    Integer code;

    String value;


    JourneyTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static JourneyTypeEnum parseEnum(Integer code) {
        JourneyTypeEnum[] journeyTypeEnums = JourneyTypeEnum.values();
        for (JourneyTypeEnum journeyTypeEnum : journeyTypeEnums) {
            if (journeyTypeEnum.getCode() == code) {
                return journeyTypeEnum;
            }
        }
        return null;
    }
}
