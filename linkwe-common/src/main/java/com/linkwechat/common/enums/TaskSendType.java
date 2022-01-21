package com.linkwechat.common.enums;

import lombok.Getter;

/**
 * 老客标签建群任务群发类型
 * @Author Hang
 * @Date 2021/3/24 9:56
 */
@Getter
public enum TaskSendType {
    /**
     * 个人群发
     */
    CROP(0, "企业群发"),

    /**
     * 企业群发
     */
    SINGLE(1, "个人群发");

    private final String name;

    private final Integer type;

    TaskSendType(Integer type, String name) {
        this.name = name;
        this.type = type;
    }
}
