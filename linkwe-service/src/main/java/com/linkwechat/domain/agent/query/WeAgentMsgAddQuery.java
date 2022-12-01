package com.linkwechat.domain.agent.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.media.WeMessageTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @date 2022年11月04日 22:33
 */
@ApiModel
@Data
public class WeAgentMsgAddQuery {
    @ApiModelProperty(hidden = true)
    private Long id;
    /**
     * 消息标题
     */
    @ApiModelProperty(value = "消息标题")
    private String msgTitle;


    @ApiModelProperty(value = "应用ID")
    private Integer agentId;

    @ApiModelProperty(value = "消息状态：0-草稿 1-待发送 2-已发送 3-发送失败 4-已撤回")
    private Integer status;

    /**
     * 范围类型 1-全部 2-自定义
     */
    @ApiModelProperty(value = "范围类型 1-全部 2-自定义")
    private Integer scopeType;


    /**
     * 接收消息的成员
     */
    @ApiModelProperty(value = "接收消息的成员",example = "['weuserid1','weuserid2','weuserid3']")
    private List<String> toUser;


    /**
     * 接收消息的部门
     */
    @ApiModelProperty(value = "接收消息的部门",example = "[1,2,3,4]")
    private List<String> toParty;


    /**
     * 接收消息的标签
     */
    @ApiModelProperty(value = "接收消息的标签",example = "[1,2,3,4]")
    private List<String> toTag;


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


    @NotNull(message = "消息不能为空")
    @ApiModelProperty(value = "消息体")
    private WeMessageTemplate weMessageTemplate;
}
