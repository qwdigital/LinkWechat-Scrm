package com.linkwechat.domain.agent.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author danmo
 * @date 2022年11月03日 18:46
 */
@ApiModel
@Data
public class WeAgentAddQuery {


    @ApiModelProperty("应用ID")
    @NotNull(message = "应用ID不能为空")
    private Integer agentId;

    @ApiModelProperty("应用密钥")
    @NotBlank(message = "应用密钥不能为空")
    private String  secret;
}
