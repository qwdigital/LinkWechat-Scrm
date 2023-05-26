package com.linkwechat.domain.qirule.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 质检规则排期员工信息
 * @date 2023/05/05 0:18
 **/
@ApiModel
@Data
public class WeQiRuleUserVo {

    @ApiModelProperty(value = "员工id")
    private String userId;

    @ApiModelProperty(value = "员工姓名")
    private String userName;
}
