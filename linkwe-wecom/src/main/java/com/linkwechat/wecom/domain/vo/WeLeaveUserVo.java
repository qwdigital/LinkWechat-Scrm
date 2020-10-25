package com.linkwechat.wecom.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:46
 */
@Data
public class WeLeaveUserVo extends BaseEntity {
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
