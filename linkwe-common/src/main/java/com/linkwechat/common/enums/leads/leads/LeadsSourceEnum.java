package com.linkwechat.common.enums.leads.leads;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 线索来源
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/08/14 9:50
 */
public enum LeadsSourceEnum {

    EXCEL(0, "Excel导入"),
    FORM(1, "智能表单"),
    MANUAL(2, "手动新增"),
    WECHAT_CUSTOMER_SERVICE(3, "微信客服"),
    ;


    private final Integer code;

    private final String source;

    private static final Map<Integer, LeadsSourceEnum> ENUM_MAP = Arrays.stream(LeadsSourceEnum.values())
            .collect(Collectors.toMap(LeadsSourceEnum::getCode, Function.identity()));

    LeadsSourceEnum(Integer code, String source) {
        this.source = source;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getSource() {
        return source;
    }

    public static LeadsSourceEnum of(int code) {
        return ENUM_MAP.get(code);
    }
}
