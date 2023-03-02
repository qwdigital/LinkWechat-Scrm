package com.linkwechat.domain.kf.query;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 场景入参
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeKfScenesQuery extends BaseEntity {

    @ApiModelProperty("场景名称")
    private String name;

    @ApiModelProperty("场景类型 1-公众号 2-小程序 3-视频号 4-搜一搜 5-微信支付 6-app 7-网页场景类型")
    private Integer type;

    @ApiModelProperty("客服帐号ID")
    private String openKfId;

    @ApiModelProperty(value = "场景ID",hidden = true)
    private List<Long> ids;
}
