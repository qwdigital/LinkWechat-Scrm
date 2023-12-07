package com.linkwechat.common.enums.message;

/**
 * 消息通知类型
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/20 14:53
 */
public enum MessageTypeEnum {

    LEADS(0, "线索动态"),
    CUSTOMER(1, "客户动态"),
    GROUP(2, "客群动态"),
    MATERIAL(3, "素材查看"),
    KF(4, "超时提醒");


    MessageTypeEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    private int code;

    private String type;

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
