package com.linkwechat.domain.wecom.query.customer.strategy;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 分页查询入参
 * @date 2021/12/2 23:08
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeCustomerStrategyQuery extends WeBaseQuery {

    /**
     * 规则组id
     */
    private Integer strategy_id;

    /**
     * 分页查询游标，首次调用可不填
     */
    private String cursor;

    /**
     * 分页大小,默认为1000，最大不超过1000
     */
    private Integer limit;

}
