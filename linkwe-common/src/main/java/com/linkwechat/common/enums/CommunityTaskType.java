package com.linkwechat.common.enums;

import lombok.Getter;

/**
 * H5列表页数据类型
 * @Author Hang
 * @Date 2021/3/24 11:02
 */
@Getter
public enum CommunityTaskType {

    TAG(1, "老客标签建群"),

    SEAS(3, "客户公海"),

    SOP(2, "群sop");


    private final String name;

    private final Integer type;

    CommunityTaskType(Integer type, String name) {
        this.name = name;
        this.type = type;
    }
}
