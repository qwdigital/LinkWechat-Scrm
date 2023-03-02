package com.linkwechat.domain.system.dept.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @date 2022年11月30日 19:29
 */
@ApiModel
@Data
public class SysDeptVo {

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "主键id")
    private Long deptId;

    /**
     * 父部门ID
     */
    @ApiModelProperty(value = "父部门id")
    private Long parentId;

    /**
     * 祖级列表
     */
    @ApiModelProperty(value = "祖级列表")
    private String ancestors;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    /**
     * 部门英文名称
     */
    @ApiModelProperty(value = "部门英文名称")
    private String deptEnName;
}
