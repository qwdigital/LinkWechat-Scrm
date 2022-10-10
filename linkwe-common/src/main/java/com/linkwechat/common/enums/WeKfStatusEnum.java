package com.linkwechat.common.enums;


import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.exception.wecom.WeComException;

/**
 * 客服消息状态
 */
public enum WeKfStatusEnum {

    UNTREATED(0, "未处理"),
    AI_RECEPTION(1, "由智能助手接待"),
    ACCESS_POOL(2, "待接入池排队中"),
    SERVICER(3, "由人工接待"),
    STAR_OR_END(4, "已结束/未开始"),
    ;
    private final int type;
    private final String msg;

    WeKfStatusEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }


    public static Boolean isEqualType(WeKfStatusEnum statusEnum, int type){
        if(ObjectUtil.equal(statusEnum.getType(),type)){
            return true;
        }else {
            return false;
        }
    }

    public static WeKfStatusEnum parseEnum(int type) {
        WeKfStatusEnum[] weKfStatusEnums = WeKfStatusEnum.values();
        for (WeKfStatusEnum weKfStatusEnum : weKfStatusEnums) {
            if (weKfStatusEnum.getType() == type) {
                return weKfStatusEnum;
            }
        }
        throw new WeComException(String.format("【%s】不是有效数据", type));
    }
}
