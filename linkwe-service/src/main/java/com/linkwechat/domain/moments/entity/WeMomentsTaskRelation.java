package com.linkwechat.domain.moments.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 朋友圈任务和企微朋友圈关联表
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/09 10:41
 */
@ApiModel
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("we_moments_task_relation")
public class WeMomentsTaskRelation {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableField("id")
    private Long id;

    /**
     * 朋友圈任务id
     */
    @ApiModelProperty(value = "朋友圈任务id")
    @TableField("moment_task_id")
    private Long momentTaskId;

    /**
     * 异步任务id，24小时有效
     */
    @ApiModelProperty(value = "异步任务id，24小时有效")
    @TableField("job_id")
    private String jobId;

    /**
     * 异步任务id失效时间
     */
    @ApiModelProperty(value = "异步任务id失效时间")
    @TableField("job_id_expire")
    private LocalDateTime jobIdExpire;

    /**
     * 朋友圈id
     */
    @ApiModelProperty(value = "朋友圈id")
    @TableField("moment_id")
    private String momentId;

}
