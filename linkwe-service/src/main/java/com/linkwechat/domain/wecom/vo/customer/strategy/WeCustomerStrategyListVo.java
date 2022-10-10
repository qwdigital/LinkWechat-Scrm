package com.linkwechat.domain.wecom.vo.customer.strategy;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 所有客户规则组id列表
 * @date 2021/12/2 23:44
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeCustomerStrategyListVo extends WeResultVo {

    private List<CustomerStrategy> strategy;

    @Data
    public static class CustomerStrategy{
        /**
         * 规则组id
         */
        private Integer strategyId;
    }
}
