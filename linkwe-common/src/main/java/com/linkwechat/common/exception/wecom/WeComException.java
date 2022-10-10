package com.linkwechat.common.exception.wecom;

/**
 * @description: 企业微信相关异常类
 * @author: My
 * @create: 2020-08-26 16:52
 **/
public class WeComException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected String message;

    private Integer code = -1;

    public WeComException(String message)
    {
        this.message = message;
    }

    public WeComException(Integer code,String message)
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
