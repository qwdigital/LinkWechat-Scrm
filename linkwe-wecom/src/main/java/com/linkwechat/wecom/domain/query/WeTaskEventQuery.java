package com.linkwechat.wecom.domain.query;

import org.springframework.context.ApplicationEvent;

/**
 * @author danmo
 * @description
 * @date 2021/11/21 19:52
 **/
public class WeTaskEventQuery extends ApplicationEvent {

    private Long businessId;

    private Integer taskSource;

    private Integer resultCode;

    public WeTaskEventQuery(Object source, Long businessId, Integer taskSource, Integer resultCode) {
        super(source);
        this.businessId = businessId;
        this.taskSource = taskSource;
        this.resultCode = resultCode;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Integer getTaskSource() {
        return taskSource;
    }

    public void setTaskSource(Integer taskSource) {
        this.taskSource = taskSource;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }
}
