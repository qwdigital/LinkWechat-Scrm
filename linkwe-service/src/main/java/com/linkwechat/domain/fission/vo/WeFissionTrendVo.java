package com.linkwechat.domain.fission.vo;

import lombok.Data;

@Data
public class WeFissionTrendVo {

    /**
     * 日期
     */
    private String date;

    /**
     * 员工邀请老客总数
     */
    private Integer inviterOldCustomerNum;

    /**
     * 完成任务老客总数
     */
    private Integer completeTaskOldCustomerNum;

    /**
     * 裂变新客总数
     */
    private Integer fissionCustomerNum;
}
