package com.linkwechat.domain;

import com.tencentcloudapi.hunyuan.v20230901.models.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class WeAiMsgQuery {

    @ApiModelProperty("会话ID")
    private String sessionId;

    @ApiModelProperty("消息列表")
    private List<Message> msgList;
}
