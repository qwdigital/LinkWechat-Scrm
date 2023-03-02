package com.linkwechat.domain.kf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客服菜单
 * @date 2022/1/18 21:57
 **/
@ApiModel
@Data
public class WeKfMenu {

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("文本-click、链接-view、小程序-miniprogram、转人工-manual")
    private String type;

    @ApiModelProperty("菜单Id")
    private String clickId;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("链接/小程序页面")
    private String url;

    @ApiModelProperty("小程序ID")
    private String appId;

    
}
