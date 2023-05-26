package com.linkwechat.domain.qirule.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.WeQiRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 会话质检通知列表出参
 *
 * @author danmo
 * @date 2023/05/05 18:22
 **/

@ApiModel
@Data
public class WeQiRuleNoticeListVo {

    @ApiModelProperty("质检通知ID")
    private Long id;

    @ApiModelProperty("质检消息ID")
    private Long qiRuleMsgId;

    @ApiModelProperty("员工ID")
    private String userId;

    @ApiModelProperty("通知类型 1-普通 2-督导")
    private Integer type;

    @ApiModelProperty("应用消息ID")
    private String msgId;

    @ApiModelProperty("质检规则ID")
    private Long ruleId;

    @ApiModelProperty("会话消息ID")
    private String chatMsgId;

    @ApiModelProperty("发送人ID")
    private String fromId;

    @ApiModelProperty("发送人名称")
    private String fromName;

    @ApiModelProperty("发送人头像")
    private String fromAvatar;

    @ApiModelProperty("性别")
    private Integer fromGender;

    @ApiModelProperty("接收人ID")
    private String receiveId;

    @ApiModelProperty("群聊ID")
    private String roomId;

    @ApiModelProperty("群聊名称")
    private String roomName;

    @ApiModelProperty("会话类型 1-客户 2-群聊")
    private Integer chatType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("消息发送时间")
    private Date sendTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("消息回复发送时间")
    private Date replyTime;

    @ApiModelProperty("会话回复消息ID")
    private String replyMsgId;

    @ApiModelProperty("会话回复状态 1-未回复 2-已回复")
    private String replyStatus;

    @ApiModelProperty("超时时间")
    private String timeMinutes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("质检通知创建时间")
    private Date createTime;

    @ApiModelProperty("质检规则名称")
    private String ruleName;

    @ApiModelProperty("消息类型")
    private String action;

    @ApiModelProperty("消息类型(如：文本，图片)")
    private String msgType;

    @ApiModelProperty("消息标识")
    private String seq;

    @ApiModelProperty("消息内容")
    private String contact;
}
