package com.linkwechat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 离职员工相关信息
 */
@Data
public class WeLeaveUser extends BaseEntity {
    private String userName;

    private String department;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dimissionTime;

    private Integer allocateCustomerNum;

    private Integer allocateGroupNum;

    private Integer isActivate;

    private String userId;

    private Integer isAllocate;
}
