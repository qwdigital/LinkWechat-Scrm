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
 * 裂变任务完成记录(WeTaskFissionCompleteRecord)
 *
 * @author danmo
 * @since 2022-07-19 10:21:08
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_task_fission_complete_record")
public class WeTaskFissionCompleteRecord extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 任务裂变表id
     */
    @ApiModelProperty(value = "任务裂变表id")
    @TableField("task_fission_id")
    private Long taskFissionId;


    /**
     * 任务裂变记录表id
     */
    @ApiModelProperty(value = "任务裂变记录表id")
    @TableField("fission_record_id")
    private Long fissionRecordId;


    /**
     * 裂变客户id
     */
    @ApiModelProperty(value = "裂变客户id")
    @TableField("customer_id")
    private String customerId;


    /**
     * 裂变客户姓名
     */
    @ApiModelProperty(value = "裂变客户姓名")
    @TableField("customer_name")
    private String customerName;


    /**
     * 状态 0 有效 1无效
     */
    @ApiModelProperty(value = "状态 0 有效 1无效")
    @TableField("status")
    private Integer status;


    /**
     * 客户头像
     */
    @ApiModelProperty(value = "客户头像")
    @TableField("customer_avatar")
    private String customerAvatar;


    /**
     * 删除标识 0 正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0 正常 1 删除")
    @TableField("del_flag")
    private Integer delFlag;
}
