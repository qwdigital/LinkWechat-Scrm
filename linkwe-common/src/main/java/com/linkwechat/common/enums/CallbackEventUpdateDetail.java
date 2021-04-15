package com.linkwechat.common.enums;

import lombok.Getter;

/**
 * 客户群变更时间的回调参数 UpdateDetail
 * 变更详情。目前有以下几种：
 * add_member : 成员入群
 * del_member : 成员退群
 * change_owner : 群主变更
 * change_name : 群名变更
 * change_notice : 群公告变更
 */
@Getter
public enum CallbackEventUpdateDetail {
    ADD_MEMBER("add_member"),
    DEL_MEMBER("del_member"),
    CHANGE_OWNER("change_owner"),
    CHANGE_NAME("change_name"),
    CHANGE_NOTICE("change_notice");

    private final String type;

    CallbackEventUpdateDetail(String type) {
        this.type = type;
    }
}
