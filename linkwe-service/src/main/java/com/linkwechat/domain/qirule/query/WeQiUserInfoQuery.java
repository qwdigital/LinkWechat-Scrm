package com.linkwechat.domain.qirule.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 活码员工排期入参
 * @date 2021/11/7 13:58
 **/
@ApiModel
@Data
public class WeQiUserInfoQuery {

    @ApiModelProperty("员工id列表")
    private List<String> userIds;

    @ApiModelProperty("工作周期")
    private List<Integer> workCycle;

    @ApiModelProperty("开始时间")
    private String beginTime;

    @ApiModelProperty("结束时间")
    private String endTime;
}
