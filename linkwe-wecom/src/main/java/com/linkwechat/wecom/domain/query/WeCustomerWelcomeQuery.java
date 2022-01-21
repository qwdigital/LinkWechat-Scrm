package com.linkwechat.wecom.domain.query;

import org.springframework.context.ApplicationEvent;

/**
 * @author danmo
 * @description 客户欢迎语
 * @date 2021/11/13 18:54
 **/
public class WeCustomerWelcomeQuery extends ApplicationEvent {

    /**
     * 客户id
     */
    private String externalUserId;

    /**
     * 员工id
     */
    private String userId;

    /**
     * 欢迎语code
     */
    private String welcomeCode;

    /**
     * 添加渠道
     */
    private String state;

    public WeCustomerWelcomeQuery(Object source, String externalUserId, String userId, String welcomeCode, String state) {
        super(source);
        this.externalUserId = externalUserId;
        this.userId = userId;
        this.welcomeCode = welcomeCode;
        this.state = state;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWelcomeCode() {
        return welcomeCode;
    }

    public void setWelcomeCode(String welcomeCode) {
        this.welcomeCode = welcomeCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
