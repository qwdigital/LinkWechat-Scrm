package com.linkwechat.common.enums;

import java.util.Objects;

/**
 * @author danmo
 * @description 数据权限枚举
 * @date 2022/5/10 18:18
 **/
public enum DataScopeType {
    /**
     * 全部数据权限
     */
    DATA_SCOPE_ALL("1"),
    /**
     * 自定数据权限
     */
    DATA_SCOPE_CUSTOM("2"),
    /**
     * 部门数据权限
     */
    DATA_SCOPE_DEPT("3"),
    /**
     * 部门及以下数据权限
     */
    DATA_SCOPE_DEPT_AND_CHILD("4"),
    /**
     * 仅本人数据权限
     */
    DATA_SCOPE_SELF("5");

    private String code;

    public String getCode() {
        return code;
    }

    DataScopeType(String code) {
        this.code = code;
    }

    public static DataScopeType of(String code) {

        Objects.requireNonNull(code, "数据范围权限类型不允许为空");

        for (DataScopeType dataScopeType : DataScopeType.values()) {
            if (dataScopeType.getCode().equals(code)) {
                return dataScopeType;
            }
        }

        throw new IllegalArgumentException(String.format("未识别的数据范围权限类型值[%s]", code));
    }
}
