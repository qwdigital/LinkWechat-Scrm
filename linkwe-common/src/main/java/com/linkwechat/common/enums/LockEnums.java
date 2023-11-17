package com.linkwechat.common.enums;


/**
 * 系统并发索标记枚举
 */
public enum LockEnums {

    WE_MOMENTS_SEND_LOCK("momentsSendKey::lock","朋友圈发送锁"),

    WE_MOMENTS_ID_LOCK("jobIdToMomentId","通过jobId换取momentsId");


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
