package com.linkwechat.domain.strategic.crowd;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class WeStrategicCrowdSwipe {

    @ApiModelProperty("下拉code值")
    private String code;

    @ApiModelProperty("输入框或选择框值")
    private String value;

    @ApiModelProperty("类型 1-渠道标签 2-企业标签 3-客户属性 4-客户行为 5-人群 6-所属群聊")
    private Integer swipType;

    @ApiModelProperty("关系值（客户属性）")
    private String relation;

    @ApiModelProperty("区间开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startTime;

    @ApiModelProperty("区结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endTime;

    @ApiModelProperty("发生类型（客户行为）")
    private String happenType;

    @ApiModelProperty("行为值（客户行为）")
    private String behavior;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("且或 1-且 2-或")
    private Integer andOr;
}
