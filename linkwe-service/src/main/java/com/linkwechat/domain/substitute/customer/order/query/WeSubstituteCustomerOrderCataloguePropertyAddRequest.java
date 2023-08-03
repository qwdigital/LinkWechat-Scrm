package com.linkwechat.domain.substitute.customer.order.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * 代客下单分类字段
 * </p>
 *
 * @author WangYX
 * @since 2023-08-03
 */
@Data
public class WeSubstituteCustomerOrderCataloguePropertyAddRequest {

    /**
     * 分类id
     */
    @NotNull(message = "分类Id必填")
    @ApiModelProperty("分类id")
    private Long catalogueId;

    /**
     * 字段名称
     */
    @Size(max = 10)
    @NotBlank(message = "字段名称必填")
    @ApiModelProperty("字段名称")
    private String name;

    /**
     * 字段类型
     *
     * @see com.linkwechat.common.enums.substitute.customer.order.SubstituteCustomerOrderCataloguePropertyTypeEnum
     */
    @NotNull(message = "字段类型必填")
    @ApiModelProperty("字段类型")
    private Integer type;

    /**
     * 是否必填 0否 1是
     */
    @NotNull(message = "是否必填不能为空")
    @ApiModelProperty("是否必填 0否 1是")
    private Integer required;

    /**
     * 字段说明
     */
    @Size(max = 20)
    @ApiModelProperty("字段说明")
    private String expound;

    /**
     * 字段值
     */
    @ApiModelProperty("字段值")
    private String value;

    /**
     * 是否金额，字段类型为数字时用，需要精确到小数点后两位  0否 1是
     */
    @ApiModelProperty("是否金额")
    private Integer money;

    /**
     * 是否精确到时间，字段类型为日期时用，0否 1是
     */
    @ApiModelProperty("是否精确到时间，字段类型为日期时用，0否 1是")
    private Integer toTime;

    /**
     * 是否多选，0否 1是
     */
    @ApiModelProperty("是否多选，0否 1是")
    private Integer multipleChoice;

    /**
     * 是否支持多个，附件时用，0否 1时
     */
    @ApiModelProperty("是否支持多个，附件时用，0否 1时")
    private Integer more;
}
