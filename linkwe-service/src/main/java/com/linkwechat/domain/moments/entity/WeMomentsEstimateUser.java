package com.linkwechat.domain.moments.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 预估朋友圈执行员工
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/26 19:23
 */
@ApiModel("预估朋友圈执行员工")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_moments_estimate_user")
public class WeMomentsEstimateUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
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
     * 提醒执行次数
     */
    @ApiModelProperty(value = "提醒执行次数")
    @TableField("execute_count")
    private Integer executeCount;

    /**
     * 执行状态:0未执行，1已执行
     */
    @ApiModelProperty(value = "执行状态:0未执行，1已执行")
    @TableField("execute_status")
    private Integer executeStatus;


}
