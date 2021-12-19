package com.linkwechat.wecom.domain.vo;

import lombok.Data;

/**
 * 客户公海统计
 */
@Data
public class CustomerSeasCountVo {

    //导入客户总数
    private Integer importCustomerTotalNum;
    //已添加客户数
    private Integer addCustomerNum;
    //完成率
    private Float completionRate;
    //等待添加客户数
    private Integer waitAddCustomerNum;
    //等待通过客户数
    private Integer waitPassCustomerNum;

}
