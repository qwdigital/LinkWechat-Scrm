package com.linkwechat.common.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author danmo
 * @description 系统错误码枚举
 * @date 2022/3/30 11:26
 **/
@NoArgsConstructor
@Getter
public enum  CommonErrorCodeEnum {

    /**
     * 登录流程异常码
     */
    ERROR_CODE_10001(-10001, "系统异常，请稍后再试"),
    ERROR_CODE_10002(-10002,"租户信息不存在,请检查是否授权"),
    ERROR_CODE_10003(-10003,"用户信息不存在,请联系管理员"),
    ERROR_CODE_10004(-10004,"请联系当前企业管理员，对该账号进行角色账号授权"),
    ERROR_CODE_10005(-10005,"请联系当前企业管理员，对该账号进行角色分配"),
    ERROR_CODE_10006(-10006,"账号已停用");



    private Integer errorCode;
    private String errorMsg;

    CommonErrorCodeEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public static CommonErrorCodeEnum parseEnum(int errorCode) {
        CommonErrorCodeEnum[] errorCodeEnums = CommonErrorCodeEnum.values();
        for (CommonErrorCodeEnum errorCodeEnum : errorCodeEnums) {
            if (errorCodeEnum.getErrorCode() == errorCode) {
                return errorCodeEnum;
            }
        }
        return null;
    }
}
