package com.linkwechat.domain.kf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客服列表
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeKfScenesVo {

    @ApiModelProperty(value = "主键id")
    private Long scenesId;

    @ApiModelProperty(value = "场景名称")
    private String scenesName;
}
