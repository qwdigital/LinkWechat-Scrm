package com.linkwechat.common.enums;


import lombok.Getter;

/**
 * 红包状态类型
 */
@Getter
public enum ReEnvelopesStateType {

    STATE_TYPE_DLQ(1,"待领取"),
    STATE_TYPE_YLQ(2,"已领取"),
    STATE_TYPE_FSSB(3,"发放失败"),
    STATE_TYPE_TKZ(4,"退款中"),
    STATE_TYPE_YTK(5,"已退款");


    private Integer key;
    private String name;

    ReEnvelopesStateType(Integer key,String name){
        this.key=key;
        this.name=name;
    }
}