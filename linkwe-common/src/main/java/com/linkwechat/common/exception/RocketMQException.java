package com.linkwechat.common.exception;

/**
 * @author danmo
 * @Description rocketmq异常类le
 * @date 2021/12/16 16:34
 **/
public class RocketMQException extends RuntimeException {

    private Integer errorCode = -1;

    private String errMsg;

    public RocketMQException(String errMsg) {
        this.errMsg = errMsg;
    }

    public RocketMQException(Integer errorCode, String errMsg) {
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
