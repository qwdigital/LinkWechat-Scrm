package com.linkwechat.common.enums;


/**
 * 群活码运用场景: 单人、多人、批量
 */
public enum WeEmpleCodeType {

    // 员工活码类型:1:单人;2:多人;3:批量;
    SINGLE(1, "单人"),
    MULTI(2, "多人"),
    BATCH(3, "批量");

    private final Integer type;
    private final String info;

    WeEmpleCodeType(Integer type, String info) {
        this.type = type;
        this.info = info;
    }

    public Integer getType() {
        return type;
    }

    public String getInfo() {
        return info;
    }
}
