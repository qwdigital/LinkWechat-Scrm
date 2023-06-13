package com.linkwechat.domain.qirule.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @description 活码列表入参
 * @date 2021/11/9 13:58
 **/
@ApiModel
@Data
public class WeQiRuleStatisticsTableListQuery {

    @ApiModelProperty(hidden = true)
    private Long ruleId;

    @ApiModelProperty("员工ID")
    private List<String> userIds;

    @ApiModelProperty("部门ID")
    private List<Long> deptIds;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("开始时间")
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("结束时间")
    private Date endTime;

}
