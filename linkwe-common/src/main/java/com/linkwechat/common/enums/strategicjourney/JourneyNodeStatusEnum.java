package com.linkwechat.common.enums.strategicjourney;

import lombok.Getter;

/**
 * 流程节点状态
 */
@Getter
public enum JourneyNodeStatusEnum {

    /**
     * 节点执行状态 0-未执行 1-正在执行 2-完成 3-失败
     */
    NOT_PERFORMED(0, "未执行"),
    IN_EXECUTION(1, "正在执行"),
    COMPLETE(2, "完成"),
    FAIL(3, "失败"),
    ;

    Integer code;

    String value;

    JourneyNodeStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static JourneyNodeStatusEnum parseEnum(Integer code) {
        JourneyNodeStatusEnum[] journeyStatusEnums = JourneyNodeStatusEnum.values();
        for (JourneyNodeStatusEnum journeyStatusEnum : journeyStatusEnums) {
            if (journeyStatusEnum.getCode() == code) {
                return journeyStatusEnum;
            }
        }
        return null;
    }

}
