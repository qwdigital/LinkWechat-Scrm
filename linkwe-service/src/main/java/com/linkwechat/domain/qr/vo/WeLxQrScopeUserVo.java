package com.linkwechat.domain.qr.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 拉新活码员工信息
 * @date 2023/03/07 0:19
 **/
@ApiModel
@Data
public class WeLxQrScopeUserVo {

    @ApiModelProperty(value = "员工类型 1-员工 2-部门")
    private Integer scopeType;

    @ApiModelProperty(value = "员工id")
    private String userId;

    @ApiModelProperty(value = "员工姓名")
    private String userName;

    @ApiModelProperty(value = "部门id")
    private Long party;

    @ApiModelProperty(value = "部门名称")
    private String partyName;

    @ApiModelProperty(value = "岗位")
    private String position;
}
