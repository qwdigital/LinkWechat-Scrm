package com.linkwechat.common.exception.wecom;

/**
 * @description: 企业微信相关异常类
 * @author: My
 * @create: 2020-08-26 16:52
 **/
public class WeComException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected final String message;
    
    public WeComException(String message)
    {
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}
