package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 策略人群信息表(WeStrategicCrowd)
 *
 * @author danmo
 * @since 2022-07-05 18:49:20
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_strategic_crowd")
public class WeStrategicCrowd extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID")
    @TableField("tenant_id")
    private Integer tenantId;


    /**
     * 人群名称
     */
    @ApiModelProperty(value = "人群名称")
    @TableField("name")
    private String name;


    /**
     * 人群分组ID
     */
    @ApiModelProperty(value = "人群分组ID")
    @TableField("group_id")
    private Long groupId;


    /**
     * 更新方式 1：手动 2：自动
     */
    @ApiModelProperty(value = "更新方式 1：手动 2：自动")
    @TableField("type")
    private Integer type;


    /**
     * 策略条件
     */
    @ApiModelProperty(value = "策略条件")
    @TableField("swipe")
    private String swipe;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;


    /**
     * 状态 1、待计算 2、计算中 3、计算完成 4、计算失败
     */
    @ApiModelProperty(value = "状态 1、待计算 2、计算中 3、计算完成 4、计算失败")
    @TableField("status")
    private Integer status;


    /**
     * 人群数量
     */
    @ApiModelProperty(value = "人群数量")
    @TableField("crowd_num")
    private Integer crowdNum;
}
