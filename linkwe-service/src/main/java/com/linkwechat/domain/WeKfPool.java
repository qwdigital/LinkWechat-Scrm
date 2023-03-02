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
import java.util.Date;

/**
 * 客服接待池表(WeKfPool)
 *
 * @author danmo
 * @since 2022-04-15 15:53:37
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_kf_pool")
public class WeKfPool extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    @TableField("corp_id")
    private String corpId;


    /**
     * 客服id
     */
    @ApiModelProperty(value = "客服id")
    @TableField("open_kf_id")
    private String openKfId;


    /**
     * 客户UserID
     */
    @ApiModelProperty(value = "客户UserID")
    @TableField("external_userid")
    private String externalUserId;


    /**
     * 状态 0-未处理,1-机器人,2-接待池,3-人工接待,4-已结束/未开始
     */
    @ApiModelProperty(value = "状态 0-未处理,1-机器人,2-接待池,3-人工接待,4-已结束/未开始")
    @TableField("status")
    private Integer status;


    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    @TableField("user_id")
    private String userId;


    /**
     * 场景值
     */
    @ApiModelProperty(value = "场景值")
    @TableField("scene")
    private String scene;


    /**
     * 进入会话时间
     */
    @ApiModelProperty(value = "进入会话时间")
    @TableField("enter_time")
    private Date enterTime;


    /**
     * 会话开始时间
     */
    @ApiModelProperty(value = "会话开始时间")
    @TableField("session_start_time")
    private Date sessionStartTime;


    /**
     * 会话结束时间
     */
    @ApiModelProperty(value = "会话结束时间")
    @TableField("session_end_time")
    private Date sessionEndTime;


    /**
     * 接待时间
     */
    @ApiModelProperty(value = "接待时间")
    @TableField("reception_time")
    private Date receptionTime;

    /**
     * 评价语类型
     */
    @ApiModelProperty(value = "评价语类型")
    @TableField("evaluation_type")
    private String evaluationType;

    /**
     * 评价语
     */
    @ApiModelProperty(value = "评价语")
    @TableField("evaluation")
    private String evaluation;


    @ApiModelProperty(value = "消息code")
    @TableField("msg_code")
    private String msgCode;

    /**
     * 是否删除:0有效,1删除
     */
    @ApiModelProperty(value = "是否删除:0有效,1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
