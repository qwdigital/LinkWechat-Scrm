package com.linkwechat.common.enums.leads.record;

/**
 * 领取方式
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/10 17:01
 */
public enum ClaimTypeEnum {

    MEMBER_TO_RECEIVE(0, "主动领取"),
    ACTIVE_ALLOCATION(1, "管理员主动分配"),
    TRANSFER(2, "线索转移"),
    ASSIGNED_ASSIGNMENT(3, "指定分配");

    private Integer code;
    private String type;

    ClaimTypeEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
