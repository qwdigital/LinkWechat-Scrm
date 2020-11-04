package com.linkwechat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 群发类型 0 发给客户 1 发给客户群
 */
@NoArgsConstructor
@Getter
public enum PushType {

    /**
     * 发给客户
     */
    SEND_TO_USER(0, "发给客户"),

    /**
     * 发给客户群
     */
    SENT_TO_USER_GROUP(1, "发给客户群");

    private String name;

    private Integer type;

    PushType(Integer type, String name) {
        this.name = name;
        this.type = type;
    }

}
