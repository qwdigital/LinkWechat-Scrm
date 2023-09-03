package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.substitute.customer.order.entity.WeSubstituteCustomerOrderCatalogueProperty;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderCataloguePropertyMoveRequest;
import com.linkwechat.domain.substitute.customer.order.vo.WeSubstituteCustomerOrderCatalogueVO;

import java.util.List;

/**
 * <p>
 * 代客下单分类字段 服务类
 * </p>
 *
 * @author WangYX
 * @since 2023-08-03
 */
public interface IWeSubstituteCustomerOrderCataloguePropertyService extends IService<WeSubstituteCustomerOrderCatalogueProperty> {

    /**
     * 移动
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/08/03 14:00
     */
    void move(WeSubstituteCustomerOrderCataloguePropertyMoveRequest request);

    /**
     * 属性
     *
     * @return {@link List< WeSubstituteCustomerOrderCatalogueVO>}
     * @author WangYX
     * @date 2023/08/03 17:05
     */
    List<WeSubstituteCustomerOrderCatalogueVO> properties();
}
