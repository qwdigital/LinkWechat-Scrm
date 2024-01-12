package com.linkwechat.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class WeAiMsgQuery extends PostBaseQuery {

    @ApiModelProperty(value = "会话ID", required = true)
    private String sessionId;

    @NotNull(message = "消息不能为空")
    @ApiModelProperty(value = "消息", required = true)
    private AiMessage msg;

    @ApiModelProperty(hidden = true)
    private Long userId;
}
