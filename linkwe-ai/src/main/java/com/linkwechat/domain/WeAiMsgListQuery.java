package com.linkwechat.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class WeAiMsgListQuery extends WeAiMsgQuery {

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty(value = "员工ID",hidden = true)
    private Long userId;

    @ApiModelProperty("收藏 0-未收藏 1-已收藏")
    private Integer collection;
}
