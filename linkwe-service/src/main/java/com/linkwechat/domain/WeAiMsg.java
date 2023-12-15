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
 * ai助手消息表(WeAiMsg)
 *
 * @author makejava
 * @since 2023-12-15 15:01:47
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
     * 收藏 0-未收藏 1-收藏
     */
    @ApiModelProperty(value = "收藏 0-未收藏 1-收藏")
    @TableField("collection")
    private Integer collection;


    /**
     * 请求消耗token数
     */
    @ApiModelProperty(value = "请求消耗token数")
    @TableField("prompt_tokens")
    private Integer promptTokens;


    /**
     * 回复消耗token数
     */
    @ApiModelProperty(value = "回复消耗token数")
    @TableField("completion_tokens")
    private Integer completionTokens;


    /**
     * 总消耗token数
     */
    @ApiModelProperty(value = "总消耗token数")
    @TableField("total_tokens")
    private Integer totalTokens;


    /**
     * 是否删除:0有效,1删除
     */
    @ApiModelProperty(value = "是否删除:0有效,1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
