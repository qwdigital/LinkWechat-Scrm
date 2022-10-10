package com.linkwechat.common.enums;


import lombok.Getter;

@Getter
public enum RedEnvelopesType {

    PERSON_RED_ENVELOPES(1,"个人红包"),
    COMPANY_RED_ENVELOPES(0,"企业红包");



    private Integer type;

    private String name;

    RedEnvelopesType(Integer type,String name){
        this.type=type;
        this.name=name;
    }


}
