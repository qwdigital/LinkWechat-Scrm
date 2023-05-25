package com.linkwechat.domain.qirule.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @description 质检周报列表入参
 * @date 2023/05/09 13:58
 **/
@ApiModel
@Data
public class WeQiRuleWeeklyListQuery {

    @ApiModelProperty(hidden = true)
    private Long weeklyId;

    @ApiModelProperty("员工名称")
    private String userName;

    @ApiModelProperty(value = "员工ID",hidden = true)
    private List<String> userIds;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("筛选时间")
    private Date dateTime;

}
