package com.linkwechat.domain.operation.vo;


import lombok.Data;

/**
 * @author danmo
 * @description 客户分析
 * @date 2022/1/9 17:09
 **/
@Data
public class WeCustomerAnalysisVo {

    //客户总数
    private int totalCnt;

    //今日跟进客户
    private int tdGjCnt;

    //客户含重复数
    private int repeatCnt;

    //今日新增客户数
    private int tdCnt;

    //昨日新增客户数
    private int ydCnt;

    //今日流失客户数
    private int tdLostCnt;

    //昨日流失客户数
    private int ydLostCnt;

    //今日净增客户
    private int tdNetCnt;

    //昨日净增客户
    private int ydNetCnt;

    //昨日申请数
    private int ydNewApplyCnt;

    //前日申请数
    private int bydNewApplyCnt;

     //今日跟进客户
    private int tdFollowUpCustomer;

    public int getYdCnt() {
        return this.tdCnt - ydCnt;
    }

    public int getYdLostCnt() {
        return this.tdLostCnt - ydLostCnt;
    }

    public int getYdNetCnt() {
        return this.tdNetCnt - ydNetCnt;
    }

    public int getBydNewApplyCnt() {
        return this.ydNewApplyCnt - bydNewApplyCnt;
    }
}
