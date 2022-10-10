package com.linkwechat.domain.customer.query;


import lombok.Data;

/**
 * 在职员工客户分配
 */
@Data
public class WeOnTheJobCustomerQuery {
    //客户id
    private String externalUserid;
    //原有人id
    private String handoverUserId;
    //接手人id
    private String takeoverUserId;
}
