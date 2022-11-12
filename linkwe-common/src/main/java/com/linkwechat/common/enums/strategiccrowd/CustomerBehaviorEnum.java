package com.linkwechat.common.enums.strategiccrowd;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户行为
 */
@Getter
public enum CustomerBehaviorEnum implements StrategicBaseEnum {

    /**
     * 添加员工
     */
    ADD_USER(1, "添加员工","qwAddUserImpl"),
    DELETE_USET(2, "删除员工","qwDeleteUserImpl"),
    ADD_GROUP_CHAT(3, "进入社群","qwAddGroupChatImpl"),
    DELETE_GROUP_CHAT(4, "退出社群","qwQuitGroupChatImpl"),
    LIKE_FRIENDS(5, "点赞朋友圈","qwLikeFriendsImpl"),
    COMMENT_FRIENDS(6, "评论朋友圈","qwCommentFriendsImpl"),
    RECEIVE_RED_ENVELOPES(7, "领取红包","qwReceiveRedEnvelopesImpl"),
    FILL_FORM(8, "填写表单","qwFillFormImpl"),
    CONSULT_KF(9, "咨询客服","qwConsultKfImpl"),
    ;

    Integer code;

    String value;

    String method;


    CustomerBehaviorEnum(Integer code, String value, String method) {
        this.code = code;
        this.value = value;
        this.method = method;
    }

    public static CustomerBehaviorEnum parseEnum(Integer code) {
        CustomerBehaviorEnum[] customerBehaviorTimeEnums = CustomerBehaviorEnum.values();
        for (CustomerBehaviorEnum customerBehaviorTimeEnum : customerBehaviorTimeEnums) {
            if (customerBehaviorTimeEnum.getCode() == code) {
                return customerBehaviorTimeEnum;
            }
        }
        return null;
    }


    public static List<Map<String, Object>> getType() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CustomerBehaviorEnum corpAddStateEnum : values()) {
            Map<String, Object> energyTypeMap = new HashMap<>(16);
            energyTypeMap.put("code", corpAddStateEnum.getCode());
            energyTypeMap.put("value", corpAddStateEnum.getValue());
            list.add(energyTypeMap);
        }
        return list;
    }
}
