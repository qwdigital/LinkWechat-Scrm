package com.linkwechat.domain.qirule.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @date 2023年05月10日 15:03
 */
@ApiModel
@Data
public class WeQiRuleStatisticsViewVo {

    @ApiModelProperty("总超时次数")
    private Integer timeOutTotalNum = 0;

    @ApiModelProperty("总超时率")
    private String timeOutTotalRate = "0";

    @ApiModelProperty("今日超时人数")
    private Long todayTimeOutUserNum = 0L;

    @ApiModelProperty("今日超时次数")
    private Integer todayTimeOutNum = 0;

    @ApiModelProperty("今日超时率")
    private String todayTimeOutRate = "0";
}
