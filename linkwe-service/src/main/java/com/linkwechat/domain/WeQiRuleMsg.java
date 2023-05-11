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
 * 质检规则会话表(WeQiRuleMsg)
 *
 * @author danmo
 * @since 2023-05-08 16:52:07
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_qi_rule_msg")
public class WeQiRuleMsg extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 规则ID
     */
    @ApiModelProperty(value = "规则ID")
    @TableField("rule_id")
    private Long ruleId;

    /**
     * 消息ID
     */
    @ApiModelProperty(value = "消息ID")
    @TableField("msg_id")
    private String msgId;


    /**
     * 发起人ID
     */
    @ApiModelProperty(value = "发起人ID")
    @TableField("from_id")
    private String fromId;


    /**
     * 接收人ID
     */
    @ApiModelProperty(value = "接收人ID")
    @TableField("receive_id")
    private String receiveId;


    /**
     * 群聊ID
     */
    @ApiModelProperty(value = "群聊ID")
    @TableField("room_id")
    private String roomId;


    /**
     * 会话类型 1-客户 2-群聊
     */
    @ApiModelProperty(value = "会话类型 1-客户 2-群聊")
    @TableField("chat_type")
    private Integer chatType;


    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    @TableField("send_time")
    private Date sendTime;


    /**
     * 回复时间
     */
    @ApiModelProperty(value = "回复时间")
    @TableField("reply_time")
    private Date replyTime;

    /**
     * 回复消息ID
     */
    @ApiModelProperty(value = "回复消息ID")
    @TableField("reply_msg_id")
    private String replyMsgId;

    /**
     * 回复状态 1-未回复 2-已回复
     */
    @ApiModelProperty(value = "回复状态 1-未回复 2-已回复")
    @TableField("reply_status")
    private Integer replyStatus;


    /**
     * 超时时间
     */
    @ApiModelProperty(value = "超时时间")
    @TableField("time_out")
    private Date timeOut;


    /**
     * 状态 0-未通知 1-已通知 2-通知失败
     */
    @ApiModelProperty(value = "状态 0-未通知 1-已通知 2-通知失败")
    @TableField("status")
    private Integer status;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
