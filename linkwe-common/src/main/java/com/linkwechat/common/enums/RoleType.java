package com.linkwechat.common.enums;

import lombok.Getter;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 企业角色与系统角色对应关系
 */
@Getter
public enum RoleType {

    WECOME_USER_TYPE_VJZ(1, "SUPPER_ADMIN", "创建者"),
    WECOME_USER_TYPE_XTLBGLY(2, "SUPPER_ADMIN", "内部系统管理员"),
    WECOME_USER_TYPE_WBXTGLY(3, "SUPPER_ADMIN", "外部系统管理员"),
    WECOME_USER_TYPE_FJGLY(4, "GRADE_ADMIN", "分级管理员"),
    WECOME_USER_TYPE_CY(5, "ORDINARY_MEMBER", "成员");


    private Integer weComeRolekey;
    private String sysRoleKey;
    private String desc;

    RoleType(Integer weComeRolekey, String sysRoleKey, String desc) {
        this.weComeRolekey = weComeRolekey;
        this.sysRoleKey = sysRoleKey;
        this.desc = desc;
    }

    public static Optional<RoleType> of(Integer weComeRolekey) {
        return Stream.of(values()).filter(s -> s.weComeRolekey.equals(weComeRolekey)).findFirst();
    }

    public static Set<String> baseRoleKeys() {
        return Stream.of(values()).map(RoleType::getSysRoleKey).collect(Collectors.toSet());
    }
}
