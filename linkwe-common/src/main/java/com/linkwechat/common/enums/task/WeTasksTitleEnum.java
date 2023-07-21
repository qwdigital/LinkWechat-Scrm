package com.linkwechat.common.enums.task;

/**
 * 任务标题
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/20 18:34
 */
public enum WeTasksTitleEnum {

    LEADS_LONG_TIME_NOT_FOLLOW_UP("线索长时间待跟进"),
    LEADS_COVENANT_WAIT_FOLLOW_UP("线索约定事项待跟进"),
    LEADS_USER_FOLLOW_UP_2_YOU("有成员的线索跟进@了你"),
    LEADS_USER_FOLLOW_UP_REPLY_YOU("有成员的线索跟进回复了你"),
    LEADS_USER_COVENANT_WAIT_FOLLOW_UP("成员的线索约定事项待跟进"),
    SOP_TODAY_WAIT_PUSH("今日SOP待推送"),
    GROUP_ADD("有1个标签建群任务待完成");

    WeTasksTitleEnum(String title) {
        this.title = title;
    }

    private String title;

    public String getTitle() {
        return title;
    }
}
