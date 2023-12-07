package com.linkwechat.common.enums.leads.template;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 线索模板-数据属性
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/06 18:52
 */
public enum DataAttrEnum {

    /**
     * 文本
     */
    TEXT("文本", 0),

    /**
     * 数字
     */
    NUMBER("数字", 1),

    /**
     * 日期
     */
    DATE("日期", 2),
    ;

    private final Integer code;

    private final String desc;

    private static final Map<Integer, DataAttrEnum> ENUM_MAP = Arrays.stream(DataAttrEnum.values())
            .collect(Collectors.toMap(DataAttrEnum::getCode, Function.identity()));

    DataAttrEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static DataAttrEnum of(int code) {
        return ENUM_MAP.get(code);
    }

    @Override
    public String toString() {
        return "DataAttrEnum{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
