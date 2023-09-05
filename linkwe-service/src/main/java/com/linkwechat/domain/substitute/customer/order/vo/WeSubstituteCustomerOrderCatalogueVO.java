package com.linkwechat.domain.substitute.customer.order.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 代客下单字段分类
 * </p>
 *
 * @author WangYX
 * @since 2023-08-02
 */
@Data
public class WeSubstituteCustomerOrderCatalogueVO {

    /**
     * 主键Id
     */
    @ApiModelProperty("主键Id")
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
    private Integer fixed;

    /**
     * 属性
     */
    @ApiModelProperty("属性")
    private List<WeSubstituteCustomerOrderCataloguePropertyVO> properties;
}
