package com.linkwechat.domain.substitute.customer.order.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 代客下单分类字段
 * </p>
 *
 * @author WangYX
 * @since 2023-08-03
 */
@Data
public class WeSubstituteCustomerOrderCataloguePropertyVO {

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
    private String code;

    /**
     * 字段类型
     *
     * @see com.linkwechat.common.enums.substitute.customer.order.SubstituteCustomerOrderCataloguePropertyTypeEnum
     */
    @ApiModelProperty("字段类型")
    private Integer type;

    /**
     * 字段类型
     */
    @ApiModelProperty("字段类型")
    private String typeStr;

    /**
     * 是否必填 0否 1是
     */
    @ApiModelProperty("是否必填 0否 1是")
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
    private Integer fixed;

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

    /**
     * 更新者
     */
    @ApiModelProperty("更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 实际值
     */
    @ApiModelProperty("实际值")
    private Object actualValue;

}
