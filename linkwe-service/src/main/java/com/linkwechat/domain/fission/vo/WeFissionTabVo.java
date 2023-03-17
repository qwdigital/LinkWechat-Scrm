package com.linkwechat.domain.fission.vo;

import lombok.Data;

@Data
public class WeFissionTabVo {

    /**
     * 员工邀请老客或客群总数
     */
    private  Integer inviterOldCustomerNum;

    /**
     * 完成任务老客总数
     */
    private Integer completeTaskOldCustomerNum;

    /**
     * 裂变新客总数
     */
    private Integer fissionCustomerNum;

    /**
     * 今日完成任务老客数
     */
    private Integer tdCompleteTaskOldCustomerNum;

    /**
     * 昨日完成任务老客数
     */
    private Integer ydCompleteTaskOldCustomerNum;

    /**
     * 昨日裂变新客数
     */
    private Integer ydFissionCustomerNum;

    /**
     * 今日裂变新客数
     */
    private Integer tdFissionCustomerNum;
}
