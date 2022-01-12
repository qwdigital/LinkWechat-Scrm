package com.linkwechat.wecom.domain;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("we_pres_tag_group")
public class WePresTagGroupTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     *主键ID
     */
    @ApiModelProperty("id")
    @TableId
    private Long taskId = SnowFlakeUtil.nextId();

    /**
     * 任务名称
     */
    @ApiModelProperty("任务名")
    @NotNull(message = "任务名称不能为空")
    private String taskName;

    /**
     * 加群引导语
     */
    @ApiModelProperty("加群引导语")
    @Size(max = 64, message = "引导与不能超过64字符")
    @NotBlank(message = "引导语不能为空")
    private String welcomeMsg;

    /**
     * 发送方式 0: 企业群发 1：个人群发
     */
    @ApiModelProperty("加群引导语")
    @NotNull(message = "发送方式不能为空")
    private Integer sendType;

    /**
     * 群活码id
     */
    @ApiModelProperty("群活码id")
    @NotNull(message = "活码不能为空")
    private Long groupCodeId;

    /**
     * 发送范围 0: 全部客户 1：部分客户
     */
    @ApiModelProperty("发送范围 0: 全部客户 1：部分客户")
    private Integer sendScope;

    /**
     * 发送性别 0: 全部 1： 男 2： 女 3：未知
     */
    @ApiModelProperty("发送性别 0: 全部 1： 男 2： 女 3：未知")
    private Integer sendGender;

    /**
     * 目标客户被添加起始时间
     */
    @ApiModelProperty("目标客户被添加起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date cusBeginTime;

    /**
     * 目标客户被添加结束时间
     */
    @ApiModelProperty("目标客户被添加结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date cusEndTime;

    /**
     * 群发消息id
     */
    @ApiModelProperty(value = "目标客户被添加结束时间", hidden = true)
    private Long messageTemplateId;

    /**
     * 逻辑删除字段
     */
    @ApiModelProperty(value = "逻辑删除字段", hidden = true)
    private Integer delFlag = 0;

    /**
     * 客户标签
     */
    @ApiModelProperty("客户标签id列表")
    @TableField(exist = false)
    private List<String> tagList;

    /**
     * 选择员工
     */
    @ApiModelProperty("员工id列表")
    @TableField(exist = false)
    private List<String> scopeList;

    /**
     * 群活码链接
     */
    @ApiModelProperty("群活码链接")
    @TableField(exist = false)
    private String codeUrl;

    /**
     * 当前群总人数
     */
    @ApiModelProperty("当前群总人数")
    @TableField(exist = false)
    private Integer totalNumber;
}
