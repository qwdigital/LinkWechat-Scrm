package com.linkwechat.common.enums.leads.template;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 线索模板-表项属性
 * @author WangYX
 * @date 2023/07/06 18:46
 * @version 1.0.0
 */
public enum TableEntryAttrEnum {

    /**
     * 填写项
     */
    INPUT("填写项", 0),

    /**
     * 下拉项
     */
    COMBOBOX("下拉项", 1);

    private final Integer code;

    private final String desc;

    private static final Map<Integer, TableEntryAttrEnum> ENUM_MAP = Arrays.stream(TableEntryAttrEnum.values())
            .collect(Collectors.toMap(TableEntryAttrEnum::getCode, Function.identity()));

    TableEntryAttrEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static TableEntryAttrEnum of(int code) {
        return ENUM_MAP.get(code);
    }

    @Override
    public String toString() {
        return "TableEntryAttrEnum{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
