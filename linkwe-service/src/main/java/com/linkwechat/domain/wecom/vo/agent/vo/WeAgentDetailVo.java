package com.linkwechat.domain.wecom.vo.agent.vo;


import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 企业应用详情接口
 */
@ApiModel
@Data
public class WeAgentDetailVo extends WeResultVo {

    //企业应用id
    @ApiModelProperty("企业应用id")
    private Integer agentId;
    //企业应用名称
    @ApiModelProperty("企业应用名称")
    private String name;
    //企业应用方形头像
    @ApiModelProperty("企业应用方形头像")
    private String squareLogoUrl;
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
    private Integer isreportenter;
    //应用主页url
    @ApiModelProperty("应用主页url")
    private String homeUrl;

    @ApiModelProperty("发布状态。0：待开发 1：开发中；2：已上线；3：存在未上线版本")
    private Integer customizedPublishStatus;

    //企业应用可见范围（人员）
    @ApiModelProperty("企业应用可见范围（人员）")
    private AllowUserInfos allowUserinfos;

    //企业应用可见范围（部门）
    @ApiModelProperty("企业应用可见范围（部门）")
    private AllowPartys allowPartys;

    @ApiModelProperty("企业应用可见范围（标签）")
    private AllowTags allowTags;

    //可见部门
    @ApiModel
    @Data
    public static class AllowPartys {
        @ApiModelProperty("可见部门ID")
        private List<String> partyId;

    }


    @Data
    @ApiModel
    public static class AllowUserInfos {
        @ApiModelProperty("可见成员ID")
        private List<AllowUser> user;
    }

    @ApiModel
    @Data
    public static class AllowTags {
        @ApiModelProperty("可见标签ID")
        private List<String> tagId;

    }

    @Data
    @ApiModel
    public static class AllowUser {
        @ApiModelProperty("成员ID")
        private String userId;
    }


}
