package com.linkwechat.domain.moments.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 预估朋友圈可见客户
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/03 10:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_moments_estimate_customer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeMomentsEstimateCustomer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键Id")
    @TableField("id")
    private Long id;

    /**
     * 朋友圈任务id
     */
    @ApiModelProperty(value = "朋友圈任务id")
    @TableField("moments_task_id")
    private Long momentsTaskId;

    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    @TableField("user_id")
    private Long userId;

    /**
     * 企微员工id
     */
    @ApiModelProperty(value = "员工id")
    @TableField("we_user_id")
    private String weUserId;

    /**
     * 员工名称
     */
    @ApiModelProperty(value = "员工名称")
    @TableField("user_name")
    private String userName;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    @TableField("external_userid")
    private String externalUserid;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    @TableField("customer_name")
    private String customerName;

    /**
     * 送达状态 0已送达 1未送达
     */
    private Integer deliveryStatus;

    /**
     * 删除标识 0:正常 1:删除
     */
    @TableLogic
    private Integer delFlag;


}
