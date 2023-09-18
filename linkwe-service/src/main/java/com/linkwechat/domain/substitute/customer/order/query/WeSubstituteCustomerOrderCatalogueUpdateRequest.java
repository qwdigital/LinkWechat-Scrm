package com.linkwechat.domain.substitute.customer.order.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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
public class WeSubstituteCustomerOrderCatalogueUpdateRequest {

    /**
     * 主键Id
     */
    @NotNull(message = "主键Id必填")
    @ApiModelProperty("主键Id")
    private Long id;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称必填！")
    @ApiModelProperty("分类名称")
    private String name;
}
