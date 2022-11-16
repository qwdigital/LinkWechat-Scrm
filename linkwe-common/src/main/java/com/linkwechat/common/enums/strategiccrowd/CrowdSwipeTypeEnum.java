package com.linkwechat.common.enums.strategiccrowd;

import lombok.Getter;

/**
 * 人群条件类型
 *
 * @author danmo
 */
@Getter
public enum CrowdSwipeTypeEnum {

    //1-渠道标签 2-企业标签 3-客户属性 4-客户行为 5-人群
    CORP_STATE(1, "渠道标签","crowdCorpStateTagServiceImpl"),

    CORP_TAG(2, "企业标签","crowdTagGroupServiceImpl"),

    CUSTOMER_ATTRIBUTES(3, "客户属性","crowdAttributesServiceImpl"),

    CUSTOMER_BEHAVIOR(4, "客户行为","crowdBehaviorServiceImpl"),

    CROWD(5, "人群","haveCrowdServiceImpl"),

    GROUPCHAT(6,"所属群聊","crowdGroupServiceImpl")
    ;

    Integer code;

    String value;


    String method;

    CrowdSwipeTypeEnum(Integer code, String value, String method) {
        this.code = code;
        this.value = value;
        this.method = method;
    }

    public static CrowdSwipeTypeEnum parseEnum(Integer code) {
        CrowdSwipeTypeEnum[] crowdSwipeTypeEnums = CrowdSwipeTypeEnum.values();
        for (CrowdSwipeTypeEnum crowdSwipeTypeEnum : crowdSwipeTypeEnums) {
            if (crowdSwipeTypeEnum.getCode() == code) {
                return crowdSwipeTypeEnum;
            }
        }
        return null;
    }
}
