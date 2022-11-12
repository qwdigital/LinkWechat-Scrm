package com.linkwechat.domain.wecom.vo.agent.vo;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class WeAgentListVo extends WeResultVo {

    @ApiModelProperty("应用列表")
    private List<Agent> agentList;

    @ApiModel
    @Data
    public static class Agent {
        //企业应用id
        @ApiModelProperty("企业应用id")
        private String agentId;
        //企业应用名称
        @ApiModelProperty("企业应用名称")
        private String name;
        //企业应用方形头像url
        @ApiModelProperty("企业应用方形头像url")
        private String squareLogoUrl;
    }


}
