package com.linkwechat.domain.qirule.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @description 活码列表入参
 * @date 2021/11/9 13:58
 **/
@ApiModel
@Data
public class WeQiRuleStatisticsTableMsgQuery {

    @ApiModelProperty("消息ID")
    private String msgId;

    @ApiModelProperty("翻页类型 0-向前加向后 1-向前 2-向后")
    private Integer pageType;

    @ApiModelProperty("条数")
    private Integer number = 50;

    @ApiModelProperty("发送人ID")
    private String fromId;

    @ApiModelProperty("接受人ID")
    private String receiveId;

    @ApiModelProperty("群聊ID")
    private String roomId;

    @ApiModelProperty("会话类型")
    private String msgType;
}
