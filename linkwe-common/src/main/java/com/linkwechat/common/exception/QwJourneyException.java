package com.linkwechat.common.exception;

/**
 * @description: 策略相关异常类
 * @author: danmo
 * @create: 2022-08-26 16:52
 **/
public class QwJourneyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected String message;

    protected Exception exception;

    private Integer code = -1;

    public QwJourneyException(String message)
    {
        this.message = message;
    }

    public QwJourneyException(Exception exception)
    {
        this.exception = exception;
    }

    public QwJourneyException(Integer code, String message)
    {
        this.code=code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
