package com.linkwechat.domain.kf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客服列表
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeKfServicerListVo {

    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("客服主键Id")
    private String kfId;

    @ApiModelProperty("客服账号Id")
    private String openKfId;

    @ApiModelProperty("员工ID")
    private String userId;

    @ApiModelProperty("员工名称")
    private String userName;

    @ApiModelProperty("接待人员的接待状态。0:接待中,1:停止接待")
    private Integer status;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门id")
    private Integer departmentId;

    @ApiModelProperty("部门名称")
    private Integer departmentName;
}
