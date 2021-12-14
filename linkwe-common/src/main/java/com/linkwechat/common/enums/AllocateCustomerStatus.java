package com.linkwechat.common.enums;

import java.util.stream.Stream;

/**
 * 客户分配的状态
 */
public enum AllocateCustomerStatus {
    JOB_EXTENDS_SUCCESS(0,"成功发起转接"),
    JOB_EXTENDS_JTWB(1,"接替完毕"),
    JOB_EXTENDS_DDJT(2,"等待接替"),
    JOB_EXTENDS_KHJJ(3,"客户拒绝"),
    JOB_EXTENDS_DDSX(4,"接替成员客户达到上限"),
    JOB_EXTENDS_WJTJL(5,"无接替记录"),
    JOB_EXTENDS_BKJC(6,"当前客户已被继承");


    private final Integer code;
    private final String info;

    AllocateCustomerStatus(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static  AllocateCustomerStatus of(Integer type){
        return Stream.of(values()).filter(s->s.getCode().equals(type)).findFirst().orElseGet(null);
    }


}
