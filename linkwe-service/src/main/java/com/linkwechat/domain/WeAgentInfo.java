package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 应用信息表(WeAgentInfo)
 *
 * @author danmo
 * @since 2022-11-04 17:08:08
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_agent_info")
public class WeAgentInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Integer id;


    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用ID")
    @TableField("agent_id")
    private Integer agentId;


    /**
     * 应用密钥
     */
    @ApiModelProperty(value = "应用密钥")
    @TableField("secret")
    private String secret;


    /**
     * 应用名称
     */
    @ApiModelProperty(value = "应用名称")
    @TableField("name")
    private String name;


    /**
     * 企业应用方形头像
     */
    @ApiModelProperty(value = "企业应用方形头像")
    @TableField("logo_url")
    private String logoUrl;


    /**
     * 应用详情
     */
    @ApiModelProperty(value = "应用详情")
    @TableField("description")
    private String description;


    /**
     * 应用可见范围员工ID
     */
    @ApiModelProperty(value = "应用可见范围员工ID")
    @TableField("allow_userinfo_id")
    private String allowUserinfoId;


    /**
     * 应用可见范围部门ID
     */
    @ApiModelProperty(value = "应用可见范围部门ID")
    @TableField("allow_party_id")
    private String allowPartyId;


    /**
     * 应用可见范围标签ID
     */
    @ApiModelProperty(value = "应用可见范围标签ID")
    @TableField("allow_tag_id")
    private String allowTagId;


    /**
     * 是否被停用
     */
    @ApiModelProperty(value = "是否被停用")
    @TableField("close")
    private Integer close;


    /**
     * 可信域名
     */
    @ApiModelProperty(value = "可信域名")
    @TableField("redirect_domain")
    private String redirectDomain;


    /**
     * 是否打开地理位置上报 0：不上报；1：进入会话上报
     */
    @ApiModelProperty(value = "是否打开地理位置上报 0：不上报；1：进入会话上报")
    @TableField("report_location_flag")
    private Integer reportLocationFlag;


    /**
     * 上报用户进入应用事件 0-不接收 1-接收
     */
    @ApiModelProperty(value = "上报用户进入应用事件 0-不接收 1-接收")
    @TableField("is_reporter")
    private Integer isReporter;


    /**
     * 应用主页url
     */
    @ApiModelProperty(value = "应用主页url")
    @TableField("home_url")
    private String homeUrl;


    /**
     * 发布状态。0-待开发 1-开发中 2-已上线 3-存在未上线版本
     */
    @ApiModelProperty(value = "发布状态。0-待开发 1-开发中 2-已上线 3-存在未上线版本")
    @TableField("customized_publish_status")
    private Integer customizedPublishStatus;

    @TableField("del_flag")
    private Integer delFlag;
}
