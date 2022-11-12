package com.linkwechat.domain.agent.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @date 2022年11月03日 18:46
 */
@ApiModel
@Data
public class WeAgentEditQuery {

    @ApiModelProperty(hidden = true)
    private Integer id;

    @ApiModelProperty("企业应用ID")
    private String agentId;

    @ApiModelProperty("企业应用名称")
    private String name;

    @ApiModelProperty("企业应用密钥")
    private String secret;

    @ApiModelProperty("企业应用详情")
    private String description;

    @ApiModelProperty("企业应用可信域名")
    private String redirectDomain;

    @ApiModelProperty("应用主页url")
    private String homeUrl;

    @ApiModelProperty("企业应用头像")
    private String logoUrl;
}
