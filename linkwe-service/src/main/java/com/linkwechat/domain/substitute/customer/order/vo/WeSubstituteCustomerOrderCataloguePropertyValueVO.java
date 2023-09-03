package com.linkwechat.domain.substitute.customer.order.vo;

import lombok.Data;

/**
 * 代客下单-自定义的属性值
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/08/07 16:32
 */
@Data
public class WeSubstituteCustomerOrderCataloguePropertyValueVO {

    /**
     * 字段Id
     */
    private Long id;

    /**
     * 值
     */
    private Object value;
}
