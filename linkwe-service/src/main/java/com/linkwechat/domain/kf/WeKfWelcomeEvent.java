package com.linkwechat.domain.kf;

import org.springframework.context.ApplicationEvent;

/**
 * @author danmo
 * @description 客服欢迎语
 * @date 2021/11/13 18:54
 **/
public class WeKfWelcomeEvent extends ApplicationEvent {

    /**
     * 客服Id
     */
    private String openKfId;


    /**
     * 欢迎语code
     */
    private String welcomeCode;

    /**
     * 消息id
     */
    private String msgId;

    /**
     * 接待方式: 1-人工客服 2-智能助手
     */
    private Integer receptionType;

    public WeKfWelcomeEvent(Object source, String openKfId, String welcomeCode, String msgId) {
        super(source);
        this.openKfId = openKfId;
        this.welcomeCode = welcomeCode;
        this.msgId = msgId;
    }

    public String getOpenKfId() {
        return openKfId;
    }

    public void setOpenKfId(String openKfId) {
        this.openKfId = openKfId;
    }

    public String getWelcomeCode() {
        return welcomeCode;
    }

    public void setWelcomeCode(String welcomeCode) {
        this.welcomeCode = welcomeCode;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
