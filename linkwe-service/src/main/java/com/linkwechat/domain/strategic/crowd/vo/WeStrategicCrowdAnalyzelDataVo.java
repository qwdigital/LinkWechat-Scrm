package com.linkwechat.domain.strategic.crowd.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class WeStrategicCrowdAnalyzelDataVo {

    @ApiModelProperty("时间")
    private String dateTime;

    @ApiModelProperty("总数")
    private Integer total;
}
