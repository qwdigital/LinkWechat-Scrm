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
 * 朋友圈员工
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/07 10:00
 */
@ApiModel("朋友圈员工")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_moments_user")
public class WeMomentsUser extends BaseEntity {

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
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    @TableField("dept_id")
    private Long deptId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    @TableField("dept_name")
    private String deptName;

    /**
     * 执行状态:0未执行，1已执行
     */
    @ApiModelProperty(value = "执行状态:0未执行，1已执行")
    @TableField("execute_status")
    private Integer executeStatus;

    /**
     * 提醒执行次数
     */
    @ApiModelProperty(value = "提醒执行次数")
    @TableField("execute_count")
    private Integer executeCount;

    /**
     * 删除标识 0:正常 1:删除
     */
    @ApiModelProperty(value = "删除标识 0:正常 1:删除")
    @TableField("del_flag")
    private Integer delFlag;

}
