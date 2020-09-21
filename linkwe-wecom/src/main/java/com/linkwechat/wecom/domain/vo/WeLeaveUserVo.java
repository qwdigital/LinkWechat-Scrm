package com.linkwechat.wecom.domain.vo;

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

    private Date dimissionTime;

    private Integer allocateCustomerNum;

    private Integer allocateGroupNum;

    private Integer isActivate;

}
