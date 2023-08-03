package com.linkwechat.domain.substitute.customer.order.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 代客下单字段分类
 * </p>
 *
 * @author WangYX
 * @since 2023-08-02
 */
@Data
public class WeSubstituteCustomerOrderCataloguePropertyMoveRequest {

    /**
     * 主键Id
     */
    @NotNull(message = "主键Id必填")
    @ApiModelProperty("主键Id")
    private Long id;

    /**
     * 分类Id
     */
    @ApiModelProperty("分类Id")
    @NotNull(message = "分类Id必填")
    private Long catalogueId;

    /**
     * 移动方向 0上 1下
     */
    @ApiModelProperty("移动方向")
    @NotNull(message = "移动方向必填")
    private Integer direction;
}
