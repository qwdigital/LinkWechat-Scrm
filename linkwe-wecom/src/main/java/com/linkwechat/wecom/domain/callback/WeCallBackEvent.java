package com.linkwechat.wecom.domain.callback;

import org.springframework.context.ApplicationEvent;

/**
 * @author danmo
 * @description
 * @date 2021/11/21 19:52
 **/
public class WeCallBackEvent extends ApplicationEvent {

    private String message;

    public WeCallBackEvent(Object source,String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
