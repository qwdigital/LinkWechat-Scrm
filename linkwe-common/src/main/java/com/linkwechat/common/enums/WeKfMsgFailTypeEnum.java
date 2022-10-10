package com.linkwechat.common.enums;


import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.exception.wecom.WeComException;

/**
 * 客服消息状态
 */
public enum WeKfMsgFailTypeEnum {

    UNKNOWN(0, "未知原因"),
    ACCOUNT_DELETED(1, "客服账号已删除"),
    APP_CLOSED(2, "应用已关闭"),
    EXPIRED(4, "会话已过期，超过48小时"),
    SESSION_CLOSED(5, "会话已关闭"),
    SESSION_LIMIT(6, "超过5条限制"),
    UNBIND_VIDEO(7, "未绑定视频号"),
    UNVERIFIED(8, "主体未验证"),
    UNBIND_VIDEO_AND_UNVERIFIED(9, "未绑定视频号且主体未验证"),
    REJECT(10, "用户拒收"),
    ;
    private final int type;
    private final String msg;

    WeKfMsgFailTypeEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public static WeKfMsgFailTypeEnum parseEnum(int type) {
        WeKfMsgFailTypeEnum[] enums = WeKfMsgFailTypeEnum.values();
        for (int i = 0; i < enums.length; i++) {
            if (enums[i].getType() == type) {
                return enums[i];
            }
        }
        throw new WeComException(String.format("【%s】不是有效类型", type));
    }

    public static Boolean isEqualType(WeKfMsgFailTypeEnum statusEnum, int type){
        if(ObjectUtil.equal(statusEnum.getType(),type)){
            return true;
        }else {
            return false;
        }
    }
}
