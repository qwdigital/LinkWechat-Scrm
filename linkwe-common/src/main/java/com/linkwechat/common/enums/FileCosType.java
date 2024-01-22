package com.linkwechat.common.enums;

import lombok.Data;

/**
 * 云存储平台类型
 */
public enum FileCosType {

    FILE_COS_TYPE_TENANT(1,"腾讯云COS"),
    FILE_COS_TYPE_ALI(2,"阿里云OSS"),
    FILE_COS_TYPE_QN(3,"七牛云Kodo"),
    FILE_COS_TYPE_MINIO(4,"minio存储");

    private Integer type;


    private String val;

    FileCosType(Integer type,String val){
        this.type=type;
        this.val=val;

    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
