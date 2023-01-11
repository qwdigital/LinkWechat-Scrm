package com.linkwechat.domain.shortlink.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author danmo
 * @description 短链新增入参
 * @date 2022/12/19 13:49
 **/
@ApiModel
@Data
public class WeShortLinkVo {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", hidden = true)
    private Long id;


    /**
     * 跳转类型 1-微信 2-其他
     */
    @ApiModelProperty(value = "跳转类型 1-微信 2-其他")
    @NotNull(message = "跳转类型不能为空")
    private Integer jumpType;


    /**
     * 推广类型 1-公众号 2-个人微信 3-企业微信 4-小程序
     */
    @ApiModelProperty(value = "推广类型 1-公众号 2-个人微信 3-企业微信 4-小程序")
    @NotNull(message = "推广类型不能为空")
    private Integer extensionType;


    /**
     * 1-文章 2-二维码
     */
    @ApiModelProperty(value = "1-文章 2-二维码")
    private Integer touchType;


    /**
     * 短链名称
     */
    @ApiModelProperty(value = "短链名称")
    @NotNull(message = "短链名称不能为空")
    private String shortLinkName;


    /**
     * 长链接
     */
    @ApiModelProperty(value = "长链接")
    private String longLink;

    /**
     * 长链接
     */
    @ApiModelProperty(value = "短链接")
    private String shortLink;


    /**
     * 业务类型 1-公众号 2-微信 3-微信群 4-员工活码 5-群活码 6-门店活码 7-小程序
     */
    @ApiModelProperty(value = "业务类型 1-公众号 2-微信 3-微信群 4-员工活码 5-群活码 6-门店活码 7-小程序")
    @NotNull(message = "业务类型不能为空")
    private Integer type;


    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;


    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String describe;


    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;


    /**
     * 二维码ID
     */
    @ApiModelProperty(value = "二维码ID")
    private String qrCodeId;


    /**
     * 二维码地址
     */
    @ApiModelProperty(value = "二维码地址")
    private String qrCode;


    /**
     * 小程序或公众号ID
     */
    @ApiModelProperty(value = "小程序或公众号ID")
    private String appId;


    /**
     * 小程序密钥
     */
    @ApiModelProperty(value = "小程序密钥")
    private String secret;

    @ApiModelProperty(value = "状态 1-启用 2-关闭")
    private Integer status;


}
