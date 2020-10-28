package com.linkwechat.wecom.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description: 已分配员工信息
 * @author: HaoN
 * @create: 2020-10-28 00:13
 **/
@Data
public class WeAllocateCustomersVo {
    /**客户名称*/
    private String customerName;

    /**接替人名称*/
    private String takeUserName;

    /**分配时间*/
    private Date allocateTime;

    /**接替人所在部门*/
    private String department;
}
