package com.linkwechat.domain.customer.vo;

import lombok.Data;

@Data
public class WeCustomerLinkCountTrendVo {

    /**
     * 时间
     */
    private String date;
    /**
     * 新增客户数
     */
    private int addWeCustomerNumber;

    /**
     * 活跃客户数
     */
    private int weCustomerActiveNumber;


}
