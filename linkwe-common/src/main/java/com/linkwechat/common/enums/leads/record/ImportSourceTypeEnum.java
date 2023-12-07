package com.linkwechat.common.enums.leads.record;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 线索导入枚举类型
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/14 13:55
 */
public enum ImportSourceTypeEnum {

    /**
     * excel
     */
    EXCEL("Excel导入", 0),

    /**
     * 智能表单
     */
    SMART_FORM("智能表单", 1),

    /**
     * 手动新增
     */
    MANUAL_ADD("手动新增", 2),

    /**
     * 微信客服
     */
    WECHAT_CUSTOMER_SERVICE("微信客服", 3),

    ;


    private final Integer code;

    private final String desc;

    private static final Map<Integer, ImportSourceTypeEnum> ENUM_MAP = Arrays.stream(ImportSourceTypeEnum.values())
            .collect(Collectors.toMap(ImportSourceTypeEnum::getCode, Function.identity()));

    ImportSourceTypeEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ImportSourceTypeEnum of(int code) {
        return ENUM_MAP.get(code);
    }

    @Override
    public String toString() {
        return "ImportSourceTypeEnum{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }

}
