package com.linkwechat.common.enums.strategiccrowd;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业添加渠道
 */
@Getter
public enum CorpAddStateEnum implements StrategicBaseEnum {

    /**
     * 企业渠道
     */
    CORP_STATE(1, "企业渠道", "qwCorpStateImpl"),
    /**
     * 员工活码
     */
    USER_QR_CODE(2, "员工活码", "qwUserQrCodeImpl"),
    /**
     * 新客拉群
     */
    NEW_CUSTOMER_GROUP(3, "新客拉群", "qwNewCustomerGroupImpl"),
    /**
     * 活动裂变
     */
    ACTIVE_FISSION(4, "活动裂变", "qwActiveFissionImpl"),

    ;

    Integer code;

    String value;

    String method;


    CorpAddStateEnum(Integer code, String value, String method) {
        this.code = code;
        this.value = value;
        this.method = method;
    }

    public static CorpAddStateEnum parseEnum(Integer code) {
        CorpAddStateEnum[] corpAddStateEnums = CorpAddStateEnum.values();
        for (CorpAddStateEnum corpAddStateEnum : corpAddStateEnums) {
            if (corpAddStateEnum.getCode() == code) {
                return corpAddStateEnum;
            }
        }
        return null;
    }


    public static List<Map<String, Object>> getType() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CorpAddStateEnum corpAddStateEnum : values()) {
            Map<String, Object> energyTypeMap = new HashMap<>(16);
            energyTypeMap.put("code", corpAddStateEnum.getCode());
            energyTypeMap.put("value", corpAddStateEnum.getValue());
            list.add(energyTypeMap);
        }
        return list;
    }
}
