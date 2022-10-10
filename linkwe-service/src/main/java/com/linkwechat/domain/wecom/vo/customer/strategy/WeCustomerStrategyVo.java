package com.linkwechat.domain.wecom.vo.customer.strategy;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 所有客户规则组id列表
 * @date 2021/12/2 23:44
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeCustomerStrategyVo extends WeResultVo {

    /**
     * 规则组id
     */
    private Integer strategyId;
}
