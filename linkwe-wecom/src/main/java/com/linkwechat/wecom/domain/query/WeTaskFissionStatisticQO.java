package com.linkwechat.wecom.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/3/8 17:55
 */
@Data
@ApiModel(value = "统计信息查询")
public class WeTaskFissionStatisticQO {
    @ApiModelProperty(value = "任务id", required = true)
    private Long taskFissionId;
    @ApiModelProperty(value = "查询最近7天数据，与thirty属性互斥，不使用请设置为false")
    private Boolean seven = true;
    @ApiModelProperty(value = "查询最近30天数据，与seven属性互斥，不使用请设置为false")
    private Boolean thirty;
    @ApiModelProperty(value = "开始时间")
    private String beginTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
