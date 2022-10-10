package com.linkwechat.common.enums;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 应用支持推送文本、图片、视频、文件、图文等类型
 */
@SuppressWarnings("all")
@Getter
public enum MessageType {

    /**
     * 文本消息
     */
    TEXT("0", "text", "文本"),
    /**
     * 图片消息
     */
    IMAGE("1", "image", "图片"),
    /**
     * 语音消息
     */
    VOICE("2", "voice", "语音"),
    /**
     * 视频消息
     */
    VIDEO("3", "video", "视频"),
    /**
     * 文件消息
     */
    FILE("4", "file", "文件"),

    /**
     * 文本卡片消息
     */
    TEXTCARD("5", "textcard", "文本卡片"),

    /**
     * 图文消息
     */
    NEWS("6", "news", "图文"),

    /**
     * 图文消息（mpnews）
     */
    MPNEWS("7", "mpnews", "图文"),

    /**
     * markdown消息
     */
    MARKDOWN("8", "markdown", "markdown"),

    /**
     * 小程序通知消息
     */
    MINIPROGRAM_NOTICE("9", "miniprogram_notice", "小程序"),

    /**
     * 任务卡片消息
     */
    TASKCARD("10", "taskcard", "任务卡片"),

    /**
     * 群发图文消息
     */
    LINK("11", "link","链接"),

    /**
     * 群发小程序
     */
    MINIPROGRAM("12", "miniprogram", "小程序"),

    ;
    /**
     * 媒体类型
     */
    String messageType;

    /**
     * 媒体名称
     */
    String name;

    /**
     * 数据值
     */
    String type;

    MessageType(String type, String messageType, String name) {
        this.type = type;
        this.messageType = messageType;
        this.name = name;
    }

    public static Optional<MessageType> of(String type) {
        return Stream.of(values()).filter(s -> s.type.equals(type)).findFirst();
    }

    public static MessageType messageTypeOf(String messageType) {
        return Stream.of(values()).filter(s -> s.messageType.equals(messageType)).findFirst().get();
    }

}
