package com.linkwechat.common.enums;


import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.exception.wecom.WeComException;

/**
 * 客服消息来源
 */
public enum WeKfOriginEnum {

    CUSTOMER_SEND(3, "微信客户发送的消息"),
    CUSTOMER_EVALUATION(31, "微信客户评价"),
    AI_SEND(4, "系统推送的事件消息"),
    SERVICER_SEND(5, "接待人员在企业微信客户端发送的消息"),
    SERVICER_WELCOME(51, "欢迎语"),
    SERVICER_END(52, "结束语"),
    SERVICER_AI(60, "机器人回复"),
    ;
    private final int type;
    private final String msg;

    WeKfOriginEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }


    public static Boolean isEqualType(WeKfOriginEnum originEnum, int type){
        if(ObjectUtil.equal(originEnum.getType(),type)){
            return true;
        }else {
            return false;
        }
    }

    public static WeKfOriginEnum parseEnum(int type) {
        WeKfOriginEnum[] weKfOriginEnums = WeKfOriginEnum.values();
        for (WeKfOriginEnum weKfOriginEnum : weKfOriginEnums) {
            if (weKfOriginEnum.getType() == type) {
                return weKfOriginEnum;
            }
        }
        throw new WeComException(String.format("【%s】不是有效数据", type));
    }
}
