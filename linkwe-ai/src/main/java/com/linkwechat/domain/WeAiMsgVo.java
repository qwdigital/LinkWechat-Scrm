package com.linkwechat.domain;

import com.tencentcloudapi.hunyuan.v20230901.models.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class WeAiMsgVo {

    @ApiModelProperty("会话ID")
    private String sessionId;

    @ApiModelProperty("消息")
    private Message msg;
}
