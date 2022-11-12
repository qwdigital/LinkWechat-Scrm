package com.linkwechat.domain.agent.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业应用详情接口
 */
@ApiModel
@Data
public class LwAgentListVo {

    @ApiModelProperty("ID")
    private Integer id;
    //企业应用id
    @ApiModelProperty("企业应用id")
    private Integer agentId;
    //企业应用名称
    @ApiModelProperty("企业应用名称")
    private String name;
    //企业应用方形头像
    @ApiModelProperty("企业应用方形头像")
    private String logoUrl;
    //企业应用详情
    @ApiModelProperty("企业应用详情")
    private String description;
    //企业应用是否被停用
    @ApiModelProperty("企业应用是否被停用")
    private Integer close;
    //企业应用可信域名
    @ApiModelProperty("企业应用可信域名")
    private String redirectDomain;
    //企业应用是否打开地理位置上报 0：不上报；1：进入会话上报；
    @ApiModelProperty("企业应用是否打开地理位置上报 0：不上报；1：进入会话上报；")
    private Integer reportLocationFlag;
    //是否上报用户进入应用事件。0：不接收；1：接收
    @ApiModelProperty("是否上报用户进入应用事件。0：不接收；1：接收")
    private Integer isReporter;
    //应用主页url
    @ApiModelProperty("应用主页url")
    private String homeUrl;

    @ApiModelProperty("发布状态。0：待开发 1：开发中；2：已上线；3：存在未上线版本")
    private Integer customizedPublishStatus;

    //企业应用可见范围（人员）
    @ApiModelProperty("企业应用可见范围（人员）")
    private String allowUserinfoId;

    @ApiModelProperty("企业应用可见范围（人员）")
    private String allowUserinfoName;

    //企业应用可见范围（部门）
    @ApiModelProperty("企业应用可见范围（部门）")
    private String allowPartyId;

    @ApiModelProperty("企业应用可见范围（部门）")
    private String allowPartyName;

    @ApiModelProperty("企业应用可见范围（标签）")
    private String allowTagId;

    @ApiModelProperty("企业应用可见范围（标签）")
    private String allowTagName;


}
