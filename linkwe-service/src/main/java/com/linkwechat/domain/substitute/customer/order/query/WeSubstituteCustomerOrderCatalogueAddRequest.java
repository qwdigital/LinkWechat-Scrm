package com.linkwechat.domain.substitute.customer.order.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 代客下单字段分类
 * </p>
 *
 * @author WangYX
 * @since 2023-08-02
 */
@Data
public class WeSubstituteCustomerOrderCatalogueAddRequest {

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称必填！")
    @ApiModelProperty("分类名称")
    private String name;
}
