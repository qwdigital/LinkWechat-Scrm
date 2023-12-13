package com.linkwechat.domain.storecode.query;

import lombok.Data;

@Data
public class WeStoreCodeQuery {

    /**
     * 门店活码id
     */
    private String storeCodeId;
    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 添加客户开始时间
     */
    private String startAddTime;


    /**
     * 添加客户结束时间
     */
    private String endAddTime;



    /**
     * 是否进群，1:是 0:否
     */
    private Integer isJoinGroup;


    /**
     * 客户渠道标识
     */
    private String shopGuideState;


    /**
     * 群渠道标识
     */
    private String groupCodeState;

    /**
     * 客户id
     */
    private String externalUserid;

}
