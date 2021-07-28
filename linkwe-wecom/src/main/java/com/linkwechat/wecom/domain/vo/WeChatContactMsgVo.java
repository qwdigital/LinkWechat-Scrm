package com.linkwechat.wecom.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @description 会话信息
 * @date 2021/7/29 0:21
 **/
@ApiModel
@Data
public class WeChatContactMsgVo {

    @ApiModelProperty("接收人id")
    private String receiver;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("消息发送时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date msgTime;

    @ApiModelProperty("消息类型(如：文本，图片)")
    private String msgType;

    @ApiModelProperty("消息类型(send(发送消息)/recall(撤回消息)/switch(切换企业日志))")
    private String action;

    @ApiModelProperty("消息内容")
    private String contact;
}
