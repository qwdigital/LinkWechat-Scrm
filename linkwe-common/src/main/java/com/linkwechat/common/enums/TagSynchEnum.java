package com.linkwechat.common.enums;

import lombok.Getter;

/**
 * 标签同步类型
 */
@Getter
public enum TagSynchEnum {

    TAG_TYPE("tag","标签"),
    GROUP_TAG_TYPE("tag_group","标签组");


    private String type;
    private String desc;

    TagSynchEnum(String type,String desc){
        this.type=type;
        this.desc=desc;
    }
}
