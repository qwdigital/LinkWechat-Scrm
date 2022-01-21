package com.linkwechat.wecom.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @description: 已分配客户信息
 * @author: HaoN
 * @create: 2020-10-28 00:13
 **/
@Data
public class WeAllocateCustomersVo extends BaseEntity {
    /**客户名称*/
    private String customerName;

    /**接替人名称*/
    private String takeUserName;

    /**分配时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date allocateTime;

    /**接替人所在部门*/
    private String department;

    /**原拥有着*/
    private String handoverUserId;


    private String externalUserid;

    private String userId;
}
