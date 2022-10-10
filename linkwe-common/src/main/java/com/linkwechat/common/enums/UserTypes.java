package com.linkwechat.common.enums;

import lombok.Getter;

@Getter
public enum UserTypes {
    USER_TYPE_SUPPER_ADMIN("00","超级管理员"),
    USER_TYPE_FJ_ADMIN("01","分级普通用户"),
    USER_TYPE_COMMON_USER("02","普通成员"),
    USER_TYPE_SELFBUILD_USER("03","自建角色成员");

    private String sysRoleKey;
    private String desc;

    UserTypes(String sysRoleKey,String desc){
        this.sysRoleKey=sysRoleKey;
        this.desc=desc;
    }
}
