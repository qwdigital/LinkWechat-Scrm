package com.linkwechat.common.enums;

import lombok.Getter;

/**
 * @author danmo
 * @description
 * @date 2022/4/14 18:37
 **/
public enum QwGroupMsgBusinessTypeEnum {

    TASK_FISSION(1, "TaskFissionGroupMsgService", "裂变任务"),
    TASK_JOURNEY(2, "TaskJourneyService", "策略旅程"),
    ;


    @Getter
    private int type;
    @Getter
    private String beanName;
    @Getter
    private String desc;

    QwGroupMsgBusinessTypeEnum(int type, String beanName, String desc) {
        this.type = type;
        this.beanName = beanName;
        this.desc = desc;
    }

    public static QwGroupMsgBusinessTypeEnum parseEnum(int type) {
        QwGroupMsgBusinessTypeEnum[] alarmGrades = QwGroupMsgBusinessTypeEnum.values();
        for (int i = 0; i < alarmGrades.length; i++) {
            if (alarmGrades[i].getType() == type) {
                return alarmGrades[i];
            }
        }
        return null;
    }
}
