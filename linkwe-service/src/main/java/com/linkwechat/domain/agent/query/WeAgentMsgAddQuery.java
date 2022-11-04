package com.linkwechat.domain.agent.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.media.WeMessageTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author sxw
 * @date 2022年11月04日 22:33
 */
@ApiModel
@Data
public class WeAgentMsgAddQuery {

    /**
     * 消息标题
     */
    @ApiModelProperty(value = "消息标题")
    private String title;


    @ApiModelProperty(value = "应用ID")
    private Integer agentId;

    /**
     * 范围类型 1-全部 2-自定义
     */
    @ApiModelProperty(value = "范围类型 1-全部 2-自定义")
    private Integer scopeType;


    /**
     * 接收消息的成员
     */
    @ApiModelProperty(value = "接收消息的成员",example = "userid1|userid2")
    private String toUser;


    /**
     * 接收消息的部门
     */
    @ApiModelProperty(value = "接收消息的部门",example = "partyid1|partyid2")
    private String toParty;


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
    private Date sendTime;


    /**
     * 计划时间
     */
    @ApiModelProperty(value = "计划时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planSendTime;


    @ApiModelProperty(value = "消息体")
    private WeMessageTemplate weMessageTemplate;
}
