package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 短链信息表(WeShortLink)
 *
 * @author danmo
 * @since 2022-12-26 11:07:16
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_short_link")
public class WeShortLink extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 跳转类型 1-微信 2-其他
     */
    @ApiModelProperty(value = "跳转类型 1-微信 2-其他")
    @TableField("jump_type")
    private Integer jumpType;


    /**
     * 推广类型 1-公众号 2-个人微信 3-企业微信 4-小程序
     */
    @ApiModelProperty(value = "推广类型 1-公众号 2-个人微信 3-企业微信 4-小程序")
    @TableField("extension_type")
    private Integer extensionType;


    /**
     * 1-文章 2-二维码
     */
    @ApiModelProperty(value = "1-文章 2-二维码")
    @TableField("touch_type")
    private Integer touchType;


    /**
     * 短链名称
     */
    @ApiModelProperty(value = "短链名称")
    @TableField("short_link_name")
    private String shortLinkName;


    /**
     * 长链接
     */
    @ApiModelProperty(value = "长链接")
    @TableField("long_link")
    private String longLink;

    /**
     * 微信跳转链接
     */
    @ApiModelProperty(value = "微信跳转链接")
    @TableField("scheme_link")
    private String schemeLink;


    /**
     * 业务类型 0-文章 1-公众号二维码 2-个人二维码 3-群二维码 4-员工活码 5-客群活码 6-门店导购活码 7-个人小程序 8-门店群活码 9-企业小程序 10-小程序二维码
     */
    @ApiModelProperty(value = "业务类型 0-文章 1-公众号二维码 2-个人二维码 3-群二维码 4-员工活码 5-客群活码 6-门店导购活码 7-个人小程序 8-门店群活码 9-企业小程序 10-小程序二维码")
    @TableField("`type`")
    private Integer type;


    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @TableField("`name`")
    private String name;


    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @TableField("`describe`")
    private String describe;


    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    @TableField("avatar")
    private String avatar;


    /**
     * 二维码ID
     */
    @ApiModelProperty(value = "二维码ID")
    @TableField("qr_code_id")
    private String qrCodeId;


    /**
     * 二维码地址
     */
    @ApiModelProperty(value = "二维码地址")
    @TableField("qr_code")
    private String qrCode;


    /**
     * 小程序或公众号ID
     */
    @ApiModelProperty(value = "小程序或公众号ID")
    @TableField("app_id")
    private String appId;


    /**
     * 小程序密钥
     */
    @ApiModelProperty(value = "小程序密钥")
    @TableField("secret")
    private String secret;


    /**
     * 状态 1-启用 2-关闭
     */
    @ApiModelProperty(value = "状态 1-启用 2-关闭")
    @TableField("status")
    private Integer status;


    /**
     * 有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "短链接有效期")
    @TableField("term_time")
    private Date termTime;

    /**
     * 删除标识 0 正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0 正常 1 删除")
    @TableField("del_flag")
    private Integer delFlag;
}
