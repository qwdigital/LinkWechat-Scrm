package com.linkwechat.common.enums;

import lombok.Getter;

/**
 * sop执行状态
 */
@Getter
public enum SopExecuteStatus {
    SOP_STATUS_ING(1,"进行中"),
    SOP_STATUS_ADVANCE(2,"提前结束"),
    SOP_STATUS_COMMON(3,"正常结束"),
    SOP_STATUS_EXCEPTION(4,"异常结束");

    Integer type;


    String value;

    SopExecuteStatus(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
