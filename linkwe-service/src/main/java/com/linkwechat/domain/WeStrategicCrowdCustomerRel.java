package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.*;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 策略人群客户关联表(WeStrategicCrowdCustomerRel)
 *
 * @author danmo
 * @since 2022-07-30 23:56:17
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_strategic_crowd_customer_rel")
public class WeStrategicCrowdCustomerRel extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID")
    @TableField("tenant_id")
    private Integer tenantId;


    /**
     * 人群ID
     */
    @ApiModelProperty(value = "人群ID")
    @TableField("crowd_id")
    private Long crowdId;


    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    @TableField("customer_id")
    private Long customerId;


    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    @TableLogic
    private Integer delFlag;
}
