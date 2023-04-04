package com.linkwechat.domain.shortlink.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author danmo
 * @description 短链新增入参
 * @date 2022/12/19 13:49
 **/
@ApiModel
@Data
public class WeShortLinkListVo {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;


    /**
     * 跳转类型 1-微信 2-其他
     */
    @ApiModelProperty(value = "跳转类型 1-微信 2-其他")
    private Integer jumpType;


    /**
     * 推广类型 1-公众号 2-个人微信 3-企业微信 4-小程序
     */
    @ApiModelProperty(value = "推广类型 1-公众号 2-个人微信 3-企业微信 4-小程序")
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
    private Integer type;

    @ApiModelProperty(value = "状态 1-启用 2-关闭")
    private Integer status;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
