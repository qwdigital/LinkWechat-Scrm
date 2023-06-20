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

import java.util.Date;

/**
 * 朋友圈互动列表
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/07 9:54
 */
@ApiModel
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
@TableName("we_moments_interacte")
public class WeMomentsInteracte extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableField("id")
    private Long id;

    /**
     * 朋友圈任务id
     */
    @ApiModelProperty(value = "朋友圈任务id")
    @TableField("moments_task_id")
    private Long momentsTaskId;

    /**
     * 企业发表成员userid
     */
    @ApiModelProperty(value = "企业发表成员userid")
    @TableField("we_user_id")
    private String weUserId;

    /**
     * 互动人员名称id
     */
    @ApiModelProperty(value = "互动人员名称id")
    @TableField("interacte_user_id")
    private String interacteUserId;

    /**
     * 互动类型:0:评论；1:点赞
     */
    @ApiModelProperty(value = "互动类型:0:评论；1:点赞")
    @TableField("interacte_type")
    private Integer interacteType;

    /**
     * 朋友圈id
     */
    @ApiModelProperty(value = "朋友圈id")
    @TableField("moment_id")
    private String momentId;

    /**
     * 互动人员类型:0:员工；1:客户
     */
    @ApiModelProperty(value = "互动人员类型:0:员工；1:客户")
    @TableField("interacte_user_type")
    private Integer interacteUserType;

    /**
     * 互动时间
     */
    @ApiModelProperty(value = "互动时间")
    @TableField("interacte_time")
    private Date interacteTime;

    /**
     * 朋友圈创建人id
     */
    @ApiModelProperty(value = "朋友圈创建人id")
    @TableField(exist = false)
    private String momentCreteOrId;

    /**
     * 删除标识 0:正常 1:删除
     */
    @ApiModelProperty(value = "删除标识 0:正常 1:删除")
    @TableField("del_flag")
    private Integer delFlag;

}
