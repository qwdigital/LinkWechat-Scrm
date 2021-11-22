package com.linkwechat.wecom.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * 活码使用范围表(WeQrScope)表实体类
 *
 * @author makejava
 * @since 2021-11-07 01:29:14
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_qr_scope")
public class WeQrScope implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 活码id
     */
    @ApiModelProperty(value = "活码id")
    @TableField("qr_id")
    private Long qrId;


    /**
     * 排期分组id
     */
    @ApiModelProperty(value = "排期分组id")
    @TableField("scope_id")
    private String scopeId;


    /**
     * 消息类型 0 默认排期 1 自定义排期
     */
    @ApiModelProperty(value = "消息类型 0 默认排期 1 自定义排期")
    @TableField("type")
    private Integer type;


    /**
     * 范围类型 1-员工 2-部门
     */
    @ApiModelProperty(value = "范围类型  1-员工 2-部门")
    @TableField("scope_type")
    private Integer scopeType;


    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    @TableField("party")
    private String party;


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


    /** 开始时间 */
    @ApiModelProperty("开始时间")
    private String beginTime;

    /** 结束时间 */
    @ApiModelProperty("结束时间")
    private String endTime;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除",hidden = true)
    @TableField("del_flag")
    private Integer delFlag;
}
