package com.linkwechat.domain.wecom.vo.customer.strategy;

import com.linkwechat.domain.wecom.entity.customer.strategy.WeCustomerStrategyRangeEntity;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 规则组管理范围
 * @date 2021/12/2 23:44
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeCustomerStrategyRangeVo extends WeResultVo {

    private List<WeCustomerStrategyRangeEntity> range;
}
