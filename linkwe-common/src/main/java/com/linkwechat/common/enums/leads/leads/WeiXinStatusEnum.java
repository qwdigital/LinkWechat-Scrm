package com.linkwechat.common.enums.leads.leads;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhaoyijie
 * @since 2023/4/20 10:44
 */
public enum WeiXinStatusEnum {

    /**
     * 验证中
     */
    VERIFYING("验证中", 0),

    /**
     * NOT_EXIST
     */
    NOT_EXIST("不存在", 1),

    /**
     * 加微信已通过
     */
    ADD_WECHAT_APPROVED("加微信已通过", 2),
    ;

    private final Integer code;

    private final String desc;

    private static final Map<Integer, WeiXinStatusEnum> ENUM_MAP = Arrays.stream(WeiXinStatusEnum.values())
            .collect(Collectors.toMap(WeiXinStatusEnum::getCode, Function.identity()));

    WeiXinStatusEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static WeiXinStatusEnum of(int code) {
        return ENUM_MAP.get(code);
    }

    @Override
    public String toString() {
        return "WeiXinStatusEnum{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }

}
