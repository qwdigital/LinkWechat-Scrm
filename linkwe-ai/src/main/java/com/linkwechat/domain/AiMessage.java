package com.linkwechat.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AiMessage {

    @ApiModelProperty("角色")
    private String role;

    @ApiModelProperty("内容")
    private String content;
}
