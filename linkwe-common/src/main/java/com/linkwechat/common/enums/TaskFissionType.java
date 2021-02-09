package com.linkwechat.common.enums;

import lombok.Getter;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/2/9 10:31
 */
@Getter
public enum TaskFissionType {
    /**
     * 任务宝
     */
    USER_FISSION(1, "任务宝"),

    /**
     * 群裂变
     */
    GROUP_FISSION(2, "群裂变");

    private String value;

    private Integer code;

    TaskFissionType(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
