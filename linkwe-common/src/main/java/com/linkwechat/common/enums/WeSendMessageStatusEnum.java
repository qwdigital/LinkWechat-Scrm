package com.linkwechat.common.enums;

import lombok.Getter;

/**
 * @author danmo
 * @date 2023年03月11日 12:35
 */
@Getter
public enum WeSendMessageStatusEnum {

    NOT_SEND("0", "未发送"),
    SEND("1", "已发送"),
    NOT_FRIEND_SEND("2", "因客户不是好友导致发送失败"),
    RECEIVE_OTHER_MESSAGE("3", "-因客户已经收到其他群发消息导致发送失败"),
    ;

    private String status;
    private String desc;

    /**
     * 构造方法
     *
     * @param status
     * @param desc
     */
    WeSendMessageStatusEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
