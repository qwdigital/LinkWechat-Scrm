package com.linkwechat.common.enums.strategicjourney;

import lombok.Getter;

/**
 * 执行状态
 */
@Getter
public enum JourneyStatusEnum {

    /**
     * 状态 0、未发布 1、待执行 2、执行中 3、已完成 4、已暂定 5、已停用 6、失败
     */
    UNPUBLISHED(0, "未发布"),
    PENDING(1, "待执行"),
    EXECUTION(2, "执行中"),
    COMPLETED(3, "已完成"),
    PAUSED(4, "已暂停"),
    TERMINATED(5, "已停用"),
    ;

    Integer code;

    String value;


    JourneyStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static JourneyStatusEnum parseEnum(Integer code) {
        JourneyStatusEnum[] journeyStatusEnums = JourneyStatusEnum.values();
        for (JourneyStatusEnum journeyStatusEnum : journeyStatusEnums) {
            if (journeyStatusEnum.getCode() == code) {
                return journeyStatusEnum;
            }
        }
        return null;
    }

}
