package com.linkwechat.common.enums;

import lombok.Getter;

/**
 * @author danmo
 * @description 接替失败的原因
 * @date 2021/1/20 0:14
 **/
@Getter
public enum TransferFailReason {
    /**
     * 客户拒绝
     */
    CUSTOMER_REFUSED(3,"customer_refused", "客户拒绝"),

    /**
     * 接替成员的客户数达到上限
     */
    CUSTOMER_LIMIT_EXCEED(4,"customer_limit_exceed", "接替成员的客户数达到上限");

    private int num;

    private String name;

    private String reason;

    TransferFailReason(int num, String name, String reason) {
        this.num = num;
        this.name = name;
        this.reason = reason;
    }

    public static String getReason(String name){
        TransferFailReason[] values = TransferFailReason.values();

        for(TransferFailReason transferFailReason:values){
            if(transferFailReason.getName().equals(name)){
                return transferFailReason.getReason();
            }
        }
        throw new RuntimeException("无对应原因");
    }

    public static int getNum(String name){
        TransferFailReason[] values = TransferFailReason.values();

        for(TransferFailReason transferFailReason:values){
            if(transferFailReason.getName().equals(name)){
                return transferFailReason.getNum();
            }
        }
        throw new RuntimeException("无对应类型");
    }
}
