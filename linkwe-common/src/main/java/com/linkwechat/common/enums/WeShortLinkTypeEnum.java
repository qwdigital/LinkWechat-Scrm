package com.linkwechat.common.enums;


import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.exception.wecom.WeComException;

/**
 * 短链类型
 */
public enum WeShortLinkTypeEnum {

    ARTICLE(0, "文章"),
    OFFICIA_QR(1, "公众号二维码"),
    PERSONAL_QR(2, "个人二维码"),
    GROUP_QR(3, "群二维码"),
    USER_QR(4, "员工活码"),
    CUSTOMER_GROUP_QR(5, "客群活码"),
    SHOPPING_QR(6, "门店导购活码"),
    PERSONAL_APPLET_QR(7, "个人小程序"),
    STORE_GROUP_QR(8, "门店群活码"),
    CORP_APPLET(9, "企业小程序"),
    APPLET_QR(10, "小程序二维码"),
    ;
    private final int type;
    private final String msg;

    WeShortLinkTypeEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }


    public static Boolean isEqualType(WeShortLinkTypeEnum statusEnum, int type){
        if(ObjectUtil.equal(statusEnum.getType(),type)){
            return true;
        }else {
            return false;
        }
    }

    public static WeShortLinkTypeEnum parseEnum(int type) {
        WeShortLinkTypeEnum[] weShortLinkTypeEnums = WeShortLinkTypeEnum.values();
        for (WeShortLinkTypeEnum weKfStatusEnum : weShortLinkTypeEnums) {
            if (weKfStatusEnum.getType() == type) {
                return weKfStatusEnum;
            }
        }
        throw new WeComException(String.format("【%s】不是有效数据", type));
    }
}
