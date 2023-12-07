package com.linkwechat.domain.kf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客服接待人员
 * @date 2022/1/18 21:57
 **/
@ApiModel
@Data
public class WeKfUser {

    @ApiModelProperty("员工id")
    private String userId;

    @ApiModelProperty("员工名称")
    private String userName;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("接待状态")
    private Integer status;
}
