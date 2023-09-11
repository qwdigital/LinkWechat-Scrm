package com.linkwechat.common.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 性别枚举
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/08/14 10:23
 */
public enum SexEnums {

    SEX_NAN(1, "男"),
    SEX_NV(2, "女"),
    SEX_WZ(0, "未知");

    private final Integer code;
    private final String info;

    SexEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static Optional<SexEnums> ofByCode(Integer code) {
        return Stream.of(values()).filter(s -> s.code.equals(code)).findFirst();
    }

    public static Optional<SexEnums> ofByInfo(String info) {
        return Stream.of(values()).filter(s -> s.info.equals(info)).findFirst();
    }
}
