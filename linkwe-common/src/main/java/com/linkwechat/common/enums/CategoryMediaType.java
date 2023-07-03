package com.linkwechat.common.enums;

import lombok.Getter;

@Getter
public enum CategoryMediaType {
    IMAGE(0, "图片"),
    VOICE(1, "语音"),
    VIDEO(2, "视频"),
    FILE(3, "普通文件"),
    TEXT(4, "文本"),
    POSTER(5, "海报"),
    LIVE(6, "活码"),
    CROWD(7,"人群"),
    TRIP(8,"旅程"),
    IMAGE_TEXT(9, "图文"),
    LINK(10, "链接"),
    APPLET(11, "小程序"),
    ARTICLE(12, "文章"),
    QY_TALK(13,"企业话术"),
    KF_TALK(14,"客服话术"),
    ZNBD(15,"智能表单"),
    SOPTLP(16,"SOP模板"),
    QFTLP(17,"群发模板"),

    ZLBDURL(18,"智能表单");


    private final Integer type;

    private final String name;

    CategoryMediaType(Integer type, String name) {
        this.name = name;
        this.type = type;
    }
}
