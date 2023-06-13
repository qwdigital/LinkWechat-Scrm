package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 质检规则范围表(WeQiRuleScope)
 *
 * @author danmo
 * @since 2023-05-05 16:57:31
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_qi_rule_scope")
public class WeQiRuleScope extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 规则id
     */
    @ApiModelProperty(value = "规则id")
    @TableField("qi_id")
    private Long qiId;

    /**
     * 排期分组id
     */
    @ApiModelProperty(value = "排期分组id")
    @TableField("scope_id")
    private String scopeId;

    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    @TableField("user_id")
    private String userId;


    /**
     * 周期时间
     */
    @ApiModelProperty(value = "周期时间")
    @TableField("work_cycle")
    private String workCycle;


    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @TableField("begin_time")
    private String beginTime;


    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @TableField("end_time")
    private String endTime;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;

}
