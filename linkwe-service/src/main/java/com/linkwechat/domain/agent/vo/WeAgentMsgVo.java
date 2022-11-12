package com.linkwechat.domain.agent.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.media.WeMessageTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 企业应用详情接口
 */
@ApiModel
@Data
public class WeAgentMsgVo {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "应用消息标题")
    private String msgTitle;

    /**
     * 范围类型 1-全部 2-自定义
     */
    @ApiModelProperty(value = "范围类型 1-全部 2-自定义")
    private Integer scopeType;


    /**
     * 接收消息的成员
     */
    @ApiModelProperty(value = "接收消息的成员",example = "userid1,userid2")
    private String toUser;
    private String toUserName;


    /**
     * 接收消息的部门
     */
    @ApiModelProperty(value = "接收消息的部门",example = "partyid1,partyid2")
    private String toParty;
    private String toPartyName;


    /**
     * 接收消息的标签
     */
    @ApiModelProperty(value = "接收消息的标签",example = "tagid1|tagid2")
    private String toTag;


    /**
     * 发送方式 1-立即发送 2-定时发送
     */
    @ApiModelProperty(value = "发送方式 1-立即发送 2-定时发送")
    private Integer sendType;


    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String sendTime;

    /**
     * 计划时间
     */
    @ApiModelProperty(value = "计划时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String planSendTime;

    /**
     * 消息状态：0-草稿 1-待发送 2-已发送 3-发送失败 4-已撤回
     */
    @ApiModelProperty(value = "消息状态：0-草稿 1-待发送 2-已发送 3-发送失败 4-已撤回")
    private Integer status;


    /**
     * 无效成员ID
     */
    @ApiModelProperty(value = "无效成员ID")
    private String invalidUser;


    /**
     * 无效部门ID
     */
    @ApiModelProperty(value = "无效部门ID")
    private String invalidParty;


    /**
     * 无效标签ID
     */
    @ApiModelProperty(value = "无效标签ID")
    private String invalidTag;


    /**
     * 没有基础接口许可(包含已过期)的userid
     */
    @ApiModelProperty(value = "没有基础接口许可(包含已过期)的userid")
    private String unlicensedUser;


    /**
     * 消息ID
     */
    @ApiModelProperty(value = "消息ID")
    private String msgId;


    /**
     * 更新模版卡片消息CODE
     */
    @ApiModelProperty(value = "更新模版卡片消息CODE")
    private String responseCode;


    @ApiModelProperty(value = "消息体")
    private WeMessageTemplate weMessageTemplate;
}
