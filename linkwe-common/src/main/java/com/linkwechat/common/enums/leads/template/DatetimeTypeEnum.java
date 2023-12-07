package com.linkwechat.common.enums.leads.template;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 线索模板-日期类型
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/06 18:53
 */
public enum DatetimeTypeEnum {

    /**
     * 日期
     */
    DATE("日期", 0, "yyyy/MM/dd"),

    /**
     * 日期+时间
     */
    DATETIME("日期+时间", 1, "yyyy/MM/dd HH:mm");

    private final Integer code;

    private final String desc;

    private final String format;

    private static final Map<Integer, DatetimeTypeEnum> ENUM_MAP = Arrays.stream(DatetimeTypeEnum.values())
            .collect(Collectors.toMap(DatetimeTypeEnum::getCode, Function.identity()));

    DatetimeTypeEnum(String desc, Integer code, String format) {
        this.desc = desc;
        this.code = code;
        this.format = format;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getFormat() {
        return format;
    }

    public static DatetimeTypeEnum of(int code) {
        return ENUM_MAP.get(code);
    }

    @Override
    public String toString() {
        return "DatetimeTypeEnum{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                ", format='" + format + '\'' +
                '}';
    }
}
