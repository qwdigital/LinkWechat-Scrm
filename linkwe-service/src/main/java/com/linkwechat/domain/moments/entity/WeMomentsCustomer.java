package com.linkwechat.domain.moments.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 朋友圈可见客户
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/07 10:00
 */
@ApiModel("朋友圈可见客户")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_moments_customer")
public class WeMomentsCustomer extends BaseEntity {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键ID")
    @TableField("id")
    private Long id;

    /**
     * 朋友圈任务id
     */
    @ApiModelProperty(value = "朋友圈任务id")
    @TableField("moments_task_id")
    private Long momentsTaskId;

    /**
     * 朋友圈id
     */
    @ApiModelProperty(value = "朋友圈id")
    @TableField("moments_id")
    private String momentsId;

    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    @TableField("user_id")
    private Long userId;

    /**
     * 企微员工id
     */
    @ApiModelProperty(value = "企微员工id")
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
    @ApiModelProperty(value = "送达状态")
    @TableField("delivery_status")
    private Integer deliveryStatus;

    /**
     * 删除标识 0:正常 1:删除
     */
    @ApiModelProperty(value = "删除标识 0:正常 1:删除")
    @TableField("del_flag")
    private Integer delFlag;

}
