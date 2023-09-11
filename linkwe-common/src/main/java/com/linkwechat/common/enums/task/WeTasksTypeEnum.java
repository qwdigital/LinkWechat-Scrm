package com.linkwechat.common.enums.task;

/**
 * 任务类型
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/20 18:16
 */
public enum WeTasksTypeEnum {

    LEADS_FOLLOW(0, "跟进"),
    SOP(1, "SOP"),
    GROUP_ADD(2, "建群");

    WeTasksTypeEnum(int code, String type) {
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
