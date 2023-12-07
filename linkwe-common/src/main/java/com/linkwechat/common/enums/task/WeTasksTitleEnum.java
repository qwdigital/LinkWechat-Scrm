package com.linkwechat.common.enums.task;

import com.linkwechat.common.enums.leads.leads.WeiXinStatusEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 任务标题
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/20 18:34
 */
public enum WeTasksTitleEnum {

    LEADS_LONG_TIME_NOT_FOLLOW_UP(0, "线索长时间待跟进"),
    LEADS_COVENANT_WAIT_FOLLOW_UP(1, "线索约定事项待跟进"),
    LEADS_USER_FOLLOW_UP_2_YOU(2, "有成员的线索跟进@了你"),
    LEADS_USER_FOLLOW_UP_REPLY_YOU(3, "有成员的线索跟进回复了你"),
    LEADS_ASSIST_USER_FOLLOW_UP_REPLY_YOU(4, "有协助跟进的成员回复了你"),
    LEADS_USER_COVENANT_WAIT_FOLLOW_UP(5, "成员的线索约定事项待跟进"),
    CUSTOMER_SOP_TODAY_WAIT_PUSH(6, "今日客户SOP待推送"),
    GROUP_SOP_TODAY_WAIT_PUSH(7, "今日客群SOP待推送"),
    GROUP_ADD_BY_LABEL(8, "有1个标签建群任务待完成");

    WeTasksTitleEnum(Integer code, String title) {
        this.code = code;
        this.title = title;
    }

    private Integer code;
    private String title;

    public Integer getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }


    private static final Map<Integer, WeTasksTitleEnum> ENUM_MAP = Arrays.stream(WeTasksTitleEnum.values())
            .collect(Collectors.toMap(WeTasksTitleEnum::getCode, Function.identity()));

    public static WeTasksTitleEnum of(int code) {
        return ENUM_MAP.get(code);
    }


}
