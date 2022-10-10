package com.linkwechat.domain.wecom.entity.customer.strategy;

import lombok.Data;

/**
 * @author danmo
 * @Description
 * @date 2021/12/3 0:13
 **/
@Data
public class WeCustomerStrategyRangeEntity {
    /**
     * 规则组id
     */
    private Integer type;

    /**
     * 管理范围内配置的成员userid，仅type为1时返回
     */
    private String userid;
    /**
     * 管理范围内配置的部门partyid，仅type为2时返回
     */
    private Integer partyid;
}
