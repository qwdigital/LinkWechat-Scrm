package com.linkwechat.domain.substitute.customer.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 代客下单字段分类
 * </p>
 *
 * @author WangYX
 * @since 2023-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_substitute_customer_order_catalogue")
public class WeSubstituteCustomerOrderCatalogue extends BaseEntity {

    /**
     * 主键Id
     */
    @ApiModelProperty("主键Id")
    @TableId("id")
    private Long id;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String name;

    /**
     * 排序
     */
    @ApiModelProperty("分类名称")
    private Integer sort;

    /**
     * 是否固定分组 0否 1是
     */
    @ApiModelProperty("是否固定分组 0否 1是")
    @TableField("is_fixed")
    private Integer fixed;

    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    private Integer delFlag;

}
