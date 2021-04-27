package com.linkwechat.common.enums;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 群发消息类型 0 文本消息  1 图片消息 2 链接消息   3 小程序消息 4 图文消息
 */
@SuppressWarnings("all")
@Getter
public enum GroupMessageType {

    /**
     * 文本消息
     */
    TEXT("0", "text"),
    /**
     * 图片消息
     */
    IMAGE("1", "image"),
    /**
     * 链接消息
     */
    LINK("2", "link"),
    /**
     * 小程序消息
     */
    MINIPROGRAM("3", "miniprogram"),
    /**
     * 图文消息
     */
    TEXT_IMAGE("4", "text_image"),

    /**
     * 视频
     */
    VIDEO("5", "video")

    ;
    /**
     * 媒体类型
     */
    String messageType;

    /**
     * 数据值
     */
    String type;

    GroupMessageType(String type, String messageType) {
        this.type = type;
        this.messageType = messageType;
    }

    public static Optional<GroupMessageType> of(String type) {
        return Stream.of(values()).filter(s -> s.type.equals(type)).findFirst();
    }

}
