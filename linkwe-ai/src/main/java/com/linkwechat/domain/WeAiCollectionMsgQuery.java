package com.linkwechat.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class WeAiCollectionMsgQuery {

    @ApiModelProperty("消息ID")
    private String msgId;

    @ApiModelProperty("收藏状态 0-未收藏 1-已收藏")
    private Integer status;
}
