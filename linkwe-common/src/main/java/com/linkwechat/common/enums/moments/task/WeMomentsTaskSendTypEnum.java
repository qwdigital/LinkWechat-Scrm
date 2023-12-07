package com.linkwechat.common.enums.moments.task;

import java.util.stream.Stream;

/**
 * 企微朋友圈发送状态枚举
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/07 15:32
 */
public enum WeMomentsTaskSendTypEnum {

    ENTERPRISE_GROUP_SEND(0, "企微群发"),
    USER_GROUP_SEND(2, "成员群发"),
    PERSON_SEND(1, "个人发送");


    private final Integer code;
    private final String msg;

    WeMomentsTaskSendTypEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static WeMomentsTaskSendTypEnum of(Integer type) {
        return Stream.of(values()).filter(s -> s.getCode().equals(type)).findFirst().orElseGet(null);
    }


}
