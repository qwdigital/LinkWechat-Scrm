package com.linkwechat.common.enums;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 媒体类型:媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件（file）
 */
@Getter
public enum MediaType {

    IMAGE("0", "image"),
    VOICE("1", "voice"),
    VIDEO("2", "video"),
    FILE("3", "file"),
    /**
     * 海报
     */
    POSTER("5", "poster"),
    /**
     * 海报字体
     */
    POSTER_FONT("6", "poster_font");

    /**
     * 媒体类型
     */
    String mediaType;

    /**
     * 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报 6 海报字体
     */
    String type;


    MediaType(String type, String mediaType) {
        this.type = type;
        this.mediaType = mediaType;
    }

    public static Optional<MediaType> of(String type) {
        return Stream.of(values()).filter(s -> s.type.equals(type)).findFirst();
    }
}
