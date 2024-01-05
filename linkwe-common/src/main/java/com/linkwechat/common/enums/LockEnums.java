package com.linkwechat.common.enums;


/**
 * 系统并发索标记枚举
 */
public enum LockEnums {

    WE_MOMENTS_SEND_LOCK("momentsSendKey::lock","朋友圈发送锁"),

    WE_FASSION_LOCK("fissionKey::lock","任务裂变发送锁"),


    WE_FORM_SURVEY_COUNT_LOCK("formSurvey::lock","表单统计锁"),

    WE_MOMENTS_ID_LOCK("jobIdToMomentId","通过jobId换取momentsId"),
    WE_FORM_SURVEY_STATE_LOCK("formSurveyState::lock","表单定时状态维护");


    private final String code;
    private final String info;


    LockEnums(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

}
