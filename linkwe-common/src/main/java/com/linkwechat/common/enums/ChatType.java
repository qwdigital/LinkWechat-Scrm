package com.linkwechat.common.enums;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * 群发任务的类型，默认为single，表示发送给客户，group表示发送给客户群
 */
@Getter
public enum  ChatType {

    /**
     * 发给客户
     */
    SINGLE("single","0"),

    /**
     * 发给客户群
     */
    GROUP("group","1")
    ;

    private String name;

    private String type;


    ChatType(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public static  ChatType of(String type){
      return Stream.of(values()).filter(s->s.getType().equals(type)).findFirst().orElseGet(null);
    }

}
