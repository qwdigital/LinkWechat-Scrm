package com.linkwechat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class AiMessage {

    @ApiModelProperty("角色")
    private String role;

    @ApiModelProperty("内容")
    private String content;
}
