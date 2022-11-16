package com.linkwechat.common.enums.strategicjourney;

import lombok.Getter;

/**
 * 流程节点类型
 */
@Getter
public enum JourneyNodeTypeEnum {

    /**
     * 节点类型 开始节点-0, 策略人群-2, 添加员工-3, 删除员工-4, 进入社群-5, 退出社群-6, 咨询客服-7, 领取红包-8, 填写表单-9, 设置标签-10,
     * 自动拉群-11, 群发消息-12, 发表朋友圈-13, 时间分支-14, 时间节点-15, 结果分支-16, 结果节点-17, 延时等待-18, 结束节点-99
     */
    INIT(0, "开始节点","qwInitNode"),
    CROWD(2, "策略人群","qwCrowdNode"),
    ADD_USER(3, "添加员工","qwAddUserNode"),
    DEL_USER(4, "删除员工","qwDelUserNode"),
    ADD_GROUP_CHAT(5, "进入社群","qwAddGroupChatNode"),
    QUITE_GROUP_CHAT(6, "退出社群","qwQuiteGroupChatNode"),
    CONSULT_KF(7, "咨询客服","qwConsultKfChatNode"),
    RECEIVE_RED_ENVELOPE(8, "领取红包","qwReceiveRedEnvelopeNode"),
    FILL_FORM(9, "填写表单","qwFillFormNode"),
    ADD_TAG(10, "设置标签","qwAddTagNode"),
    AUTO_BUILD_GROUP_CHAT(11, "自动拉群","qwAutoBuildGroupChatNode"),
    GROUP_MSG(12, "群发消息","qwGroupMsgNode"),
    FRIENDS(13, "发表朋友圈","qwFriendsNode"),
    TIME_BRANCH(14, "时间分支","qwTimeBranchNode"),
    TIME_NODE(15, "时间节点","qwTimeNode"),
    RESULT_BRANCH(16, "结果分支","qwResultBranchNode"),
    RESUL_NODE(17, "结果节点","qwResultNode"),
    DELAY(18, "延时等待","qwDelayNode"),
    FRIEND_INTERACTIVE(19, "朋友圈互动","qwFriendInteractiveNode"),
    END(99, "结束节点","qwEndNode"),
    ;

    Integer code;

    String value;

    String method;


    JourneyNodeTypeEnum(Integer code, String value, String method) {
        this.code = code;
        this.value = value;
        this.method = method;
    }

    public static JourneyNodeTypeEnum parseEnum(Integer code) {
        JourneyNodeTypeEnum[] journeyStatusEnums = JourneyNodeTypeEnum.values();
        for (JourneyNodeTypeEnum journeyStatusEnum : journeyStatusEnums) {
            if (journeyStatusEnum.getCode() == code) {
                return journeyStatusEnum;
            }
        }
        return null;
    }

}
