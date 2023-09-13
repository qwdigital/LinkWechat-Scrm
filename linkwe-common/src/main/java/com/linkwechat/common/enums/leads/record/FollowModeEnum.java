package com.linkwechat.common.enums.leads.record;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 跟进方式枚举
 *
 * @author zhaoyijie
 * @since 2023/4/19 14:29
 */
public enum FollowModeEnum {

    /**
     * 电话联系
     */
    PHONE_CALL("电话联系", 0),

    /**
     * 企微沟通
     */
    WECHAT_COMMUNICATION("企微沟通", 1),

    /**
     * 短信跟进
     */
    SMS_FOLLOW_UP("短信跟进", 2),

    /**
     * 现场沟通
     */
    SCENE_COMMUNICATION("现场沟通", 3),

    /**
     * 其他跟进
     */
    OTHER_FOLLOW_UP("其他跟进", 4),
    ;

    private final Integer code;

    private final String desc;

    private static final Map<Integer, FollowModeEnum> ENUM_MAP = Arrays.stream(FollowModeEnum.values())
            .collect(Collectors.toMap(FollowModeEnum::getCode, Function.identity()));

    FollowModeEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static FollowModeEnum of(int code) {
        return ENUM_MAP.get(code);
    }

    @Override
    public String toString() {
        return "FollowModeEnum{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }

}
