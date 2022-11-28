package com.linkwechat.common.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum LiveStateEnums {

    LIVE_STATE_YYZ(0,"预约中"),
    LIVE_STATE_ZBZ(1,"直播中"),
    LIVE_STATE_YJS(2,"已结束"),
    LIVE_STATE_YGQ(3,"已过期"),
    LIVE_STATE_YQX(4,"已取消")

    ;

    private final Integer code;
    private final String info;

    LiveStateEnums(Integer code, String info)
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

    public static Optional<LiveStateEnums> ofByCode(Integer code) {
        return Stream.of(values()).filter(s -> s.code.equals(code)).findFirst();
    }

    public static Optional<LiveStateEnums> ofByInfo(String info) {
        return Stream.of(values()).filter(s -> s.info.equals(info)).findFirst();
    }
}
