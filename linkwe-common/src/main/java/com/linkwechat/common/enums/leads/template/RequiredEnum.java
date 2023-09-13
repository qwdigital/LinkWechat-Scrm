package com.linkwechat.common.enums.leads.template;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 线索模板-是否必填
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/06 18:55
 */
public enum RequiredEnum {

    /**
     * 非必需
     */
    UN_REQUIRED("非必需", 0),

    /**
     * 必需
     */
    REQUIRED("必需", 1),
    ;

    private final Integer code;

    private final String desc;

    private static final Map<Integer, RequiredEnum> ENUM_MAP = Arrays.stream(RequiredEnum.values())
            .collect(Collectors.toMap(RequiredEnum::getCode, Function.identity()));

    RequiredEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static RequiredEnum of(int code) {
        return ENUM_MAP.get(code);
    }

    @Override
    public String toString() {
        return "RequiredEnum{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
