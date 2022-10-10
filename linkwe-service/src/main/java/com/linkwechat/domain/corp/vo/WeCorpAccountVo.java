package com.linkwechat.domain.corp.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @description 企业信息
 * @date 2022/4/11 22:57
 **/
@ApiModel
@Data
public class WeCorpAccountVo {

    @ApiModelProperty("租户ID")
    private Integer id;

    @ApiModelProperty("企业名称")
    private String corpName;

    @ApiModelProperty("企业ID")
    private String corpId;

    @ApiModelProperty("三方企业ID")
    private String openCorpId;

    @ApiModelProperty("会话密钥")
    private String chatSecret;



    @ApiModelProperty("通知开关")
    private Integer noticeSwitch;

    @ApiModelProperty("应用ID")
    private String suiteId;

    @ApiModelProperty("企业类型 认证号：verified, 注册号：unverified")
    private String corpType;

    @ApiModelProperty("授权方企业方形头像")
    private String corpSquareLogoUrl;

    @ApiModelProperty("授权方企业用户规模")
    private Integer corpUserMax;

    @ApiModelProperty("授权方企业的主体名称 即企业全称")
    private String corpFullName;

    @ApiModelProperty("企业类型 1. 企业; 2. 政府以及事业单位; 3. 其他组织, 4.团队号")
    private Integer subjectType;

    @ApiModelProperty("认证到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date verifiedEndTime;

    @ApiModelProperty("授权企业在微工作台（原企业号）的二维码 可用于关注微工作台")
    private String corpWxqrcode;

    @ApiModelProperty("企业规模")
    private String corpScale;

    @ApiModelProperty("企业所属行业")
    private String corpIndustry;

    @ApiModelProperty("企业所属子行业")
    private String corpSubIndustry;

    @ApiModelProperty("注册码")
    private String registerCode;

    @ApiModelProperty("推广包ID")
    private String templateId;

    @ApiModelProperty("仅当获取注册码指定该字段时才返回")
    private String registerState;

    @ApiModelProperty("安装应用时，扫码或者授权链接中带的state值")
    private String state;

    @ApiModelProperty("应用id")
    private String agentId;

    @ApiModelProperty("应用名字")
    private String agentName;

    @ApiModelProperty("应用方形头像")
    private String squareLogoUrl;

    @ApiModelProperty("应用圆形头像")
    private String roundLogoUrl;

    @ApiModelProperty("授权模式，0为管理员授权；1为成员授权")
    private Integer authMode;

    @ApiModelProperty("是否为代开发自建应用")
    private Integer isCustomizedApp;

    @ApiModelProperty("权限等级 1-通讯录基本信息只读2-通讯录全部信息只读（已废弃）3-通讯录全部信息读写4-单个基本信息只读5:通讯录全部信息只写（已废弃）")
    private Integer level;

    @ApiModelProperty("应用可见范围（部门）")
    private String allowParty;

    @ApiModelProperty("应用可见范围（标签）")
    private String allowTag;

    @ApiModelProperty("应用可见范围（成员）")
    private String allow_user;

    /**
     * 消息加密公钥
     */
    private String financePrivateKey;

    //商户名称
    private String merChantName;

    //商户号
    private String merChantNumber;

    //商户密钥
    private String merChantSecret;

    //API证书文件p12
    private String certP12Url;

}
