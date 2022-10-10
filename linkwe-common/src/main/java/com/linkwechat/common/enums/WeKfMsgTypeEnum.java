package com.linkwechat.common.enums;


/**
 * 客服消息类型
 */
public enum WeKfMsgTypeEnum {

    // 员工活码类型:1:单人;2:多人;3:批量;
    TEXT("text", "文本消息"),
    IMAGE("image", "图片消息"),
    VOICE("voice", "语音消息"),
    VIDEO("video", "视频消息"),
    FILE("file", "文件消息"),
    LOCATION("location", "位置消息"),
    LINK("link", "链接消息"),
    BUSINESS_CARD("business_card", "名片消息"),
    MINIPROGRAM("miniprogram", "小程序消息"),
    MSGMENU("msgmenu", "菜单消息"),
    EVENT("event", "事件消息");

    private final String type;
    private final String msg;

    WeKfMsgTypeEnum(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }
}
