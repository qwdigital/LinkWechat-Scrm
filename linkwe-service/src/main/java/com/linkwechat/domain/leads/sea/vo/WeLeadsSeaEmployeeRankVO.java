package com.linkwechat.domain.leads.sea.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 公海统计-跟进员工
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/17 15:30
 */
@Data
public class WeLeadsSeaEmployeeRankVO {

    @ApiModelProperty("员工名称")
    private String userName;

    @ApiModelProperty("今日有效跟进")
    private Integer todayFollowNum;
}
