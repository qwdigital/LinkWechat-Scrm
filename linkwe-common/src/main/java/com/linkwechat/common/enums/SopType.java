package com.linkwechat.common.enums;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum SopType {
    SOP_TYPE_XK(1,"新客sop"),
    SOP_TYPE_HDJR(2,"活动节日sop"),
    SOP_TYPE_KHZH(3,"客户转化sop"),
    SOP_TYPE_XQPY(4,"新群培育sop"),
    SOP_TYPE_ZQYX(5,"周期营销sop"),
    SOP_TYPE_TDXF(6,"特定宣发sop");

    private Integer sopKey;
    private String sopVal;

    SopType(Integer sopKey,String sopVal){
        this.sopKey=sopKey;
        this.sopVal=sopVal;
    }

    public static Optional<SopType> of(Integer sopKey) {
        return Stream.of(values()).filter(s -> s.sopKey.equals(sopKey)).findFirst();
    }


}
