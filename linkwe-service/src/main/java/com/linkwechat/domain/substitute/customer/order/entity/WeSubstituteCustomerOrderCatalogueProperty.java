package com.linkwechat.domain.substitute.customer.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 代客下单分类字段
 * </p>
 *
 * @author WangYX
 * @since 2023-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_substitute_customer_order_catalogue_property")
public class WeSubstituteCustomerOrderCatalogueProperty extends BaseEntity {

    /**
     * 主键id
     */
    @ApiModelProperty("主键Id")
    @TableId("id")
    private Long id;

    /**
     * 分类id
     */
    @ApiModelProperty("分类id")
    private Long catalogueId;

    /**
     * 字段名称
     */
    @ApiModelProperty("字段名称")
    private String name;

    /**
     * 字段编码
     */
    @ApiModelProperty("字段编码")
    @TableField("`code`")
    private String code;

    /**
     * 字段类型
     *
     * @see com.linkwechat.common.enums.substitute.customer.order.SubstituteCustomerOrderCataloguePropertyTypeEnum
     */
    @ApiModelProperty("字段类型")
    private Integer type;

    /**
     * 是否必填 0否 1是
     */
    @ApiModelProperty("是否必填 0否 1是")
    @TableField("is_require")
    private Integer required;

    /**
     * 字段说明
     */
    @ApiModelProperty("字段说明")
    private String expound;

    /**
     * 字段值
     */
    @ApiModelProperty("字段值")
    @TableField(value = "`value`")
    private String value;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 是否固定字段，0否 1是
     */
    @ApiModelProperty("是否固定字段，0否 1是")
    @TableField("is_fixed")
    private Integer fixed;

    /**
     * 是否金额，字段类型为数字时用，需要精确到小数点后两位  0否 1是
     */
    @ApiModelProperty("是否金额")
    @TableField("is_money")
    private Integer money;

    /**
     * 是否精确到时间，字段类型为日期时用，0否 1是
     */
    @ApiModelProperty("是否精确到时间，字段类型为日期时用，0否 1是")
    @TableField("is_to_time")
    private Integer toTime;

    /**
     * 是否多选，0否 1是
     */
    @ApiModelProperty("是否多选，0否 1是")
    @TableField("is_multiple_choice")
    private Integer multipleChoice;

    /**
     * 是否支持多个，附件时用，0否 1时
     */
    @ApiModelProperty("是否支持多个，附件时用，0否 1时")
    @TableField("is_more")
    private Integer more;

    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    private Integer delFlag;

}
