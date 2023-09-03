package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.substitute.customer.order.entity.WeSubstituteCustomerOrder;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderRequest;
import com.linkwechat.domain.substitute.customer.order.vo.WeSubstituteCustomerOrderVO;

import java.util.List;

/**
 * <p>
 * 代客下单-订单 Mapper 接口
 * </p>
 *
 * @author WangYX
 * @since 2023-08-03
 */
public interface WeSubstituteCustomerOrderMapper extends BaseMapper<WeSubstituteCustomerOrder> {

    /**
     * 订单列表
     *
     * @param request 请求参数
     * @return {@link List < WeSubstituteCustomerOrderVO >}
     * @author WangYX
     * @date 2023/08/21 16:25
     */
    List<WeSubstituteCustomerOrderVO> list(WeSubstituteCustomerOrderRequest request);

}
