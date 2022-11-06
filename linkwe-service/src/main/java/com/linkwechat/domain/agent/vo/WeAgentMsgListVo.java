package com.linkwechat.domain.agent.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 企业应用详情接口
 */
@ApiModel
@Data
public class WeAgentMsgListVo {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("消息类型")
    private String msgType;

    @ApiModelProperty("消息状态：0-草稿 1-待发送 2-已发送 3-发送失败 4-已撤回")
    private String status;

    @ApiModelProperty("发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String sendTime;

    @ApiModelProperty(value = "发送方式 1-立即发送 2-定时发送")
    private Integer sendType;

    @ApiModelProperty(value = "计划时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planSendTime;
}
