package com.linkwechat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class WeAiCollectionMsgVo {

    @ApiModelProperty("会话ID")
    private String sessionId;

    @ApiModelProperty("AI对话消息ID")
    private String msgId;

    @ApiModelProperty("对话内容")
    private List<WeAiMsgVo> contentList;
}
