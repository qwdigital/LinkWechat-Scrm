package com.linkwechat.domain.kf.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @description 场景列表
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeKfRecordVo {

    @ApiModelProperty(value = "消息ID")
    private String msgId;

    @ApiModelProperty(value = "客服账号ID")
    private String openKfId;

    @ApiModelProperty(value = "客服名称")
    private String kfName;

    @ApiModelProperty(value = "客服头像")
    private String kfAvatar;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户头像")
    private String customerAvatar;

    @ApiModelProperty(value = "客户ID")
    private String externalUserId;

    @ApiModelProperty(value = "消息来源:3-客户发送的消息 5-接待人员发送的消息")
    private Integer origin;

    @ApiModelProperty(value = "发送时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    @ApiModelProperty(value = "消息类型 text、image、voice、video、file、location、link、business_card、miniprogram、msgmenu")
    private String msgType;

    @ApiModelProperty(value = "内容")
    private String content;

}
