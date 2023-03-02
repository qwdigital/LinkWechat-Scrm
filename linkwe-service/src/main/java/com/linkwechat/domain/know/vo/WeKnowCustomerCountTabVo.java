package com.linkwechat.domain.know.vo;

import lombok.Data;


@Data
public class WeKnowCustomerCountTabVo {

    /**
     *
     * 今日新客扫码人数
     */
    private Long tdNewCustomerScanNumber;

    /**
     *
     * 昨日新客扫码人数
     */
    private Long ydNewCustomerScanNumber;


    /**
     * 今日老客扫码人数
     */
    private Long tdOldCustomerScanNumber;

    /**
     * 昨日老客扫码人数
     */
    private Long ydOldCustomerScanNumber;

    /**
     * 今日添加新客人数
     */
    private Long tdAddCustomerNumber;


    /**
     * 昨日添加新客人数
     */
    private Long ydAddCustomerNumber;

    /**
     * 今日流失客户人数
     */
    private Long tdLostCustomerNumber;


    /**
     * 昨日流失客户人数
     */
    private Long ydLostCustomerNumber;



}
