package com.linkwechat.common.enums.leads.template;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 线索公海-是否可被编辑
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/06 18:57
 */
public enum CanEditEnum {

    /**
     * 可被编辑
     */
    ALLOW("可被编辑", 0),

    /**
     * 不可被编辑
     */
    DISALLOWED("不可被编辑", 1),

    ;

    private final Integer code;

    private final String desc;

    private static final Map<Integer, CanEditEnum> ENUM_MAP = Arrays.stream(CanEditEnum.values())
            .collect(Collectors.toMap(CanEditEnum::getCode, Function.identity()));

    CanEditEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CanEditEnum of(int code) {
        return ENUM_MAP.get(code);
    }

    @Override
    public String toString() {
        return "CanEditEnum{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
