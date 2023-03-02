package com.linkwechat.common.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum WhetherEnums {

    YES(1,"是"),
    NO(0,"否"),
    WZ(2,"未知")
    ;

    private final Integer code;
    private final String info;

    WhetherEnums(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static Optional<WhetherEnums> ofByCode(Integer code) {
        return Stream.of(values()).filter(s -> s.code.equals(code)).findFirst();
    }

    public static Optional<WhetherEnums> ofByInfo(String info) {
        return Stream.of(values()).filter(s -> s.info.equals(info)).findFirst();
    }
}
