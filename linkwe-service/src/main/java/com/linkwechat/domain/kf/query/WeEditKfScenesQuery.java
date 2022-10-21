package com.linkwechat.domain.kf.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author danmo
 * @description 新增客服入参
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeEditKfScenesQuery {

    @NotNull(message = "场景ID不能为空")
    @ApiModelProperty("场景ID")
    private Long id;

    @NotEmpty(message = "场景名称不能为空")
    @ApiModelProperty("场景名称")
    private String name;

    @NotNull(message = "场景类型不能为空")
    @ApiModelProperty("场景类型 1-公众号 2-小程序 3-视频号 4-搜一搜 5-微信支付 6-app 7-网页场景类型")
    private Integer type;

    @NotNull(message = "客服不能为空")
    @ApiModelProperty("客服id")
    private Long kfId;

    @NotNull(message = "客服帐号ID不能为空")
    @ApiModelProperty("客服帐号ID")
    private String openKfId;

}
