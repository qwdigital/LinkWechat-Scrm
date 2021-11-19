package com.linkwechat.wecom.domain.vo;


import lombok.Data;

import java.util.Date;


/**
 * 公海导入记录
 */
@Data
public class CustomerSeasRecordVo {
    //导入表格名称
    private String tableExcelName;
    //导入客户总数
    private Integer cutomerTotalNum;
    //导入时间
    private Date createTime;
    //分配员工
    private String addUserName;
    //已添加客户数
    private Integer addCustomerNum;
    //待添加客户数
    private Integer waitAddCustomerNum;
    //待通过客户数
    private Integer waitPassCustomerNum;
    //添加完成率
    private Float completionRate;
 }
