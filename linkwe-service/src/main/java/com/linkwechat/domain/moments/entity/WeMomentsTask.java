package com.linkwechat.domain.moments.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 朋友圈
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/06 17:26
 */
@ApiModel
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
@TableName("we_moments_task")
public class WeMomentsTask extends BaseEntity {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableField("id")
    private Long id;

    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    @TableField("name")
    private String name;

    /**
     * 朋友圈类型:0:企业动态;1:个人动态
     */
    @ApiModelProperty(value = "朋友圈类型:0:企业动态;1:个人动态")
    @TableField("type")
    private Integer type;

    /**
     * 发送方式: 0企微群发,1个人发送,2成员群发,
     */
    @ApiModelProperty(value = "发送方式: 0企微群发，2成员群发")
    @TableField("send_type")
    private Integer sendType;

    /**
     * 是否是在lw平台发布的:1:是;0:否;
     */
    @ApiModelProperty(value = "是否是在lw平台发布的:1:是;0:否;")
    @TableField("is_lw_push")
    private Integer isLwPush;

    /**
     * 发送范围: 0全部客户 1按条件筛选
     */
    @ApiModelProperty(value = "发送范围: 0全部客户 1按条件筛选")
    @TableField("scope_type")
    private Integer scopeType;

    /**
     * 朋友圈可见客户数
     */
    @ApiModelProperty(value = "朋友圈可见客户数")
    @TableField("customer_num")
    private Integer customerNum;

    /**
     * 部门id集合
     */
    @ApiModelProperty(value = "部门id集合")
    @TableField("dept_ids")
    private String deptIds;

    /**
     * 岗位id集合
     */
    @ApiModelProperty(value = "岗位id集合")
    @TableField("post_ids")
    private String postIds;

    /**
     * 员工id集合
     */
    @ApiModelProperty(value = "员工id集合")
    @TableField("user_ids")
    private String userIds;

    /**
     * 客户标签
     */
    @ApiModelProperty(value = "客户标签")
    @TableField("customer_tag")
    private String customerTag;

    /**
     * 朋友圈文本内容
     */
    @ApiModelProperty(value = "朋友圈文本内容")
    @TableField("content")
    private String content;

    /**
     * 执行时间
     */
    @ApiModelProperty(value = "执行时间")
    @TableField("execute_time")
    private LocalDateTime executeTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @TableField("execute_end_time")
    private LocalDateTime executeEndTime;

    /**
     * 点赞标签
     */
    @ApiModelProperty(value = "点赞标签")
    @TableField("like_tag_ids")
    private String likeTagIds;

    /**
     * 评论标签
     */
    @ApiModelProperty(value = "评论标签")
    @TableField("comment_tag_ids")
    private String commentTagIds;

    /**
     * 任务状态：1未开始，2进行中，3已结束
     */
    @ApiModelProperty(value = "任务状态：1未开始，2进行中，3已结束")
    @TableField("status")
    private Integer status;

    /**
     * 删除标识 0:正常 1:删除
     */
    @ApiModelProperty(value = "删除标识 0:正常 1:删除")
    @TableField("del_flag")
    private Integer delFlag;

    /**
     * 企微创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("establish_time")
    private Date establishTime;
}
