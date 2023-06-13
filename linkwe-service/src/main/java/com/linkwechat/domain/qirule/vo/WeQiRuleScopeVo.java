package com.linkwechat.domain.qirule.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @date 2023/05/05 18:22
 **/
@ApiModel
@Data
public class WeQiRuleScopeVo {

    @ApiModelProperty(value = "活码id")
    private Long qiId;

    @ApiModelProperty(value = "周期时间")
    private String workCycle;

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;


    @ApiModelProperty("员工姓名")
    private List<WeQiRuleUserVo> weQiRuleUserList;

}
