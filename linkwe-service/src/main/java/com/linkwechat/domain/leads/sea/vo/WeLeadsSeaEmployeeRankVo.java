package com.linkwechat.domain.leads.sea.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author caleb
 * @since 2023/5/9
 */
@Data
public class WeLeadsSeaEmployeeRankVo {

    @ApiModelProperty("员工名称")
    private String userName;

    @ApiModelProperty("今日有效跟进")
    private Integer todayFollowNum;
}
