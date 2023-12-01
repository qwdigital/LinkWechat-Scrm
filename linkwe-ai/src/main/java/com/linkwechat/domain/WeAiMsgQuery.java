package com.linkwechat.domain;

import com.tencentcloudapi.hunyuan.v20230901.models.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@ApiModel
@Data
public class WeAiMsgQuery {

    @NotEmpty(message = "会话ID不能为空")
    @ApiModelProperty(value = "会话ID",required = true)
    private String sessionId;

    @ApiModelProperty(value = "消息列表",required = true)
    @Size(min = 1, max = 40, message = "消息列表最少为1个最长为40")
    private List<AiMessage> msgList;
}
