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
 * ai助手消息表(WeAiMsg)
 *
 * @author makejava
 * @since 2023-12-04 10:01:27
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_ai_msg")
public class WeAiMsg extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 会话ID
     */
    @ApiModelProperty(value = "会话ID")
    @TableField("session_id")
    private String sessionId;


    /**
     * AI对话ID
     */
    @ApiModelProperty(value = "AI对话ID")
    @TableField("msg_id")
    private String msgId;


    /**
     * 员工ID
     */
    @ApiModelProperty(value = "员工ID")
    @TableField("user_id")
    private Long userId;


    /**
     * 角色
     */
    @ApiModelProperty(value = "角色")
    @TableField("role")
    private String role;


    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    @TableField("content")
    private String content;


    /**
     * 结果ID
     */
    @ApiModelProperty(value = "结果ID")
    @TableField("request_id")
    private String requestId;


    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    @TableField("send_time")
    private Date sendTime;


    /**
     * 免责声明
     */
    @ApiModelProperty(value = "免责声明")
    @TableField("note")
    private String note;


    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id")
    @TableField("create_by_id")
    private Long createById;


    /**
     * 更新人id
     */
    @ApiModelProperty(value = "更新人id")
    @TableField("update_by_id")
    private Long updateById;


    /**
     * 是否删除:0有效,1删除
     */
    @ApiModelProperty(value = "是否删除:0有效,1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
