package com.linkwechat.domain.qr.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 活码排期员工信息
 * @date 2021/11/8 0:18
 **/
@ApiModel
@Data
public class WeQrScopeUserVo {
    @ApiModelProperty(value = "员工id")
    private String userId;

    @ApiModelProperty(value = "员工姓名")
    private String userName;

    @ApiModelProperty(value = "排班次数")
    private Integer schedulingNum;

    @ApiModelProperty(value = "是否备用员工 0：否 1：是")
    private Integer isSpareUser;
}
