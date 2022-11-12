package com.linkwechat.domain.wecom.vo.agent.query;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class WeAgentQuery extends WeBaseQuery {
    //企业应用名称
    @ApiModelProperty("企业应用名称")
    private String name;
    //企业应用详情
    @ApiModelProperty("企业应用详情")
    private String description;
    //企业应用可信域名
    @ApiModelProperty("企业应用可信域名")
    private String redirect_domain;
    //企业应用是否打开地理位置上报 0：不上报；1：进入会话上报；
    @ApiModelProperty("企业应用是否打开地理位置上报 0：不上报；1：进入会话上报")
    private Integer report_location_flag;
    //是否上报用户进入应用事件。0：不接收；1：接收
    @ApiModelProperty("是否上报用户进入应用事件。0：不接收；1：接收")
    private Integer isreportenter;
    //应用主页url
    @ApiModelProperty("应用主页url")
    private String home_url;
    //企业应用头像的mediaid，通过素材管理接口上传图片获得mediaid，上传后会自动裁剪成方形和圆形两个头像
    @ApiModelProperty("企业应用头像")
    private String logo_mediaid;


}
