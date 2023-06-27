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


}
