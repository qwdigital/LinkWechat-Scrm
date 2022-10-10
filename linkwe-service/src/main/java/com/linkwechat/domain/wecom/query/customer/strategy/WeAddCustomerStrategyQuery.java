package com.linkwechat.domain.wecom.query.customer.strategy;

import com.linkwechat.domain.wecom.entity.customer.strategy.WeCustomerStrategyPrivilegeEntity;
import com.linkwechat.domain.wecom.entity.customer.strategy.WeCustomerStrategyRangeEntity;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 分页查询入参
 * @date 2021/12/2 23:08
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeAddCustomerStrategyQuery extends WeBaseQuery {

    /**
     * 父规则组id
     */
    private Integer parent_id;

    /**
     * 规则组名称
     */
    private String strategy_name;

    /**
     * 规则组管理员userid列表，不可配置超级管理员，每个规则组最多可配置20个负责人
     */
    private List<String> admin_list;

    /**
     * 基础权限
     */
    private WeCustomerStrategyPrivilegeEntity privilege;

    /**
     * 管理范围
     */
    private WeCustomerStrategyRangeEntity range;
}
