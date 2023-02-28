package com.linkwechat.domain.strategic.crowd.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class WeStrategicCrowdAnalyzelVo {

    @ApiModelProperty("今日净增人数")
    private Integer netAddNum;

    @ApiModelProperty("今日新增人数")
    private Integer addNum;

    @ApiModelProperty("今日减少人数")
    private Integer reduceNum;

    @ApiModelProperty("数据")
    private List<WeStrategicCrowdAnalyzelDataVo> crowdAnalyzels;
}
