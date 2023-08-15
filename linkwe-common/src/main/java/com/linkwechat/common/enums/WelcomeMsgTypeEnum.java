package com.linkwechat.common.enums;


import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.enums.strategiccrowd.CorpAddStateEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客服消息状态
 */
@Getter
public enum WelcomeMsgTypeEnum {

    /**
     * 活码前缀
     */
    WE_QR_CODE_PREFIX("we_qr", "weQrCodeMsgServiceImpl"),

    /**
     * 拉新活码前缀
     */
    WE_LX_QR_CODE_PREFIX("we_lxqr", "weLxQrCodeMsgServiceImpl"),

    /**
     * 门店导购员或群前缀
     */
    WE_STORE_CODE_CONFIG_PREFIX("we_sc_conf", "weStoreQrCodeMsgServiceImpl"),

    /**
     * 识客码前缀
     */
    WE_KNOW_CUSTOMER_CODE_PREFIX("we_kn_code", "weKnowCustomerMsgServiceImpl"),


    /**
     * 获客链接前缀
     */
    WE_CUSTOMER_LINK_PREFIX("we_link","weCustomerLinkMsgServiceImpl"),

    /**
     * 新客拉群前缀
     */
    WE_QR_XKLQ_PREFIX("we_xklq", "weXklqQrCodeMsgServiceImpl"),

    /**
     * 任务宝前缀
     */
    FISSION_PREFIX("fis-", "weFissionMsgServiceImpl"),

    /**
     * 默认
     */
    DEFAULT("default", "weDefaultQrCodeMsgServiceImpl");


    private final String type;
    private final String value;

    WelcomeMsgTypeEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }


    public static Boolean isEqualType(WelcomeMsgTypeEnum msgTypeEnum, String type){
        if(ObjectUtil.equal(msgTypeEnum.getType(),type)){
            return true;
        }else {
            return false;
        }
    }

    public static WelcomeMsgTypeEnum parseEnum(String type) {
        WelcomeMsgTypeEnum[] welcomeMsgTypeEnums = WelcomeMsgTypeEnum.values();
        for (WelcomeMsgTypeEnum welcomeMsgTypeEnum : welcomeMsgTypeEnums) {
            if (welcomeMsgTypeEnum.getType() == type) {
                return welcomeMsgTypeEnum;
            }
        }
        throw new WeComException(String.format("【%s】不是有效数据", type));
    }

    public static Map<String, String> getMap() {
        Map<String, String> typeMap = new HashMap<>(16);
        for (WelcomeMsgTypeEnum welcomeMsgTypeEnum : values()) {
            typeMap.put(welcomeMsgTypeEnum.getType(),welcomeMsgTypeEnum.getValue());
        }
        return typeMap;
    }
}
