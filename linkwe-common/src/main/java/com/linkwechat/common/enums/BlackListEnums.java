package com.linkwechat.common.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum BlackListEnums {

    //加入黑名单
    IS_JOIN_BLACKLIST(0,"是"),
    //不加入黑名单
    IS_NOT_JOIN_BLACKLIST(1,"否");

    private final Integer code;
    private final String info;

    BlackListEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static Optional<BlackListEnums> ofByCode(Integer code) {
        return Stream.of(values()).filter(s -> s.code.equals(code)).findFirst();
    }

    public static Optional<BlackListEnums> ofByInfo(String info) {
        return Stream.of(values()).filter(s -> s.info.equals(info)).findFirst();
    }

}
