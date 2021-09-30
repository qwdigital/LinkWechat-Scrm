package com.linkwechat.wecom.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/3/8 10:24
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "每日数据")
public class WeTaskFissionDailyDataVO {
    @ApiModelProperty(value = "日期")
    private String day;
    @ApiModelProperty(value = "日新增客户")
    private Integer increase = 0;
    @ApiModelProperty(value = "日参与活动客户")
    private Integer attend = 0;
    @ApiModelProperty(value = "日完成任务客户")
    private Integer complete = 0;
}
