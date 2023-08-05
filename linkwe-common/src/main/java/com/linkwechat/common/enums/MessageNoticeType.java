package com.linkwechat.common.enums;

import lombok.Getter;

/**
 * 消息通知类型
 * @Author Hang
 * @Date 2021/3/24 11:02
 */
@Getter
public enum MessageNoticeType {

    TAG(1, "老客标签建群"),

    SEAS(3, "客户公海"),

    ADDCUTOMER(4,"客户添加员工"),

    CUSTOMERADDCHAT(6,"客户或成员入群"),

    CUSTOMEREXITCHAT(7,"客户或成员退群"),

    DAILYPUSH(8,"每日统计推送"),

    DELETEWEUSER(5,"客户删除员工"),

    CUSTOMER_SOP(9,"客户sop"),
    GROUP_SOP(10,"客群sop"),

    GROUP_SOP_DQTX(11,"客群sop到期提醒"),
    CUSTOMER_SOP_DQTX(12,"客户sop到期提醒"),

    SOP(2, "群sop");


    private final String name;

    private final Integer type;

    MessageNoticeType(Integer type, String name) {
        this.name = name;
        this.type = type;
    }
}
