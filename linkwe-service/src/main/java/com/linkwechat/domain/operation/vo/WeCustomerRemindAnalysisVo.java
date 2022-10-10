package com.linkwechat.domain.operation.vo;

import lombok.Data;


/**
 * 客户提醒相关数据分析
 */
@Data
public class WeCustomerRemindAnalysisVo {

    //昨日客户总数
     int ydTotalCnt;

    //昨日新增客户
    int ydCnt;

    //昨日跟进客户
    int ydFollowUpCustomer;

    //昨日净增客户
    int ydNetCnt;

    //昨日流失客户
    int ydLostCnt;

    //昨日发送申请
    int ydNewApplyCnt;

}
