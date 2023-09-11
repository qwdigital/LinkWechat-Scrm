package com.linkwechat.common.enums.message;

/**
 * 消息是否已读
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/11 14:26
 */
public enum MessageReadEnum {


    READ(1, "已读"),
    UN_READ(0, "未读");

    private Integer code;
    private String msg;

    MessageReadEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
