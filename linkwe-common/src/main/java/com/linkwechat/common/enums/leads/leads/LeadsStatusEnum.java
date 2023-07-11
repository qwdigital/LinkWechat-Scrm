package com.linkwechat.common.enums.leads.leads;

import com.linkwechat.common.exception.ServiceException;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 线索跟进状态枚举
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/04 16:24
 */
public enum LeadsStatusEnum {

    WAIT_FOR_DISTRIBUTION(0, "waitForDistribution", "待分配"),
    BE_FOLLOWING_UP(1, "beFollowingUp", "跟进中"),
    VISIT(2, "visit", "已转化"),
    RETURNED(3, "returned", "已退回");

    private final Integer code;
    private final String statusEn;
    private final String statusCn;

    LeadsStatusEnum(Integer code, String statusEn, String statusCn) {
        this.code = code;
        this.statusEn = statusEn;
        this.statusCn = statusCn;
    }

    public Integer getCode() {
        return code;
    }

    public String getStatusEn() {
        return statusEn;
    }

    public String getStatusCn() {
        return statusCn;
    }


    public static LeadsStatusEnum ofByCode(Integer code) {
        Optional<LeadsStatusEnum> first = Stream.of(values()).filter(s -> s.code.equals(code)).findFirst();
        return first.orElseThrow(() -> new ServiceException("线索跟进状态不存在！"));
    }


}
