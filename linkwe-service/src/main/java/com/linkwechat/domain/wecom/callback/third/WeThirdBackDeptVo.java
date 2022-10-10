package com.linkwechat.domain.wecom.callback.third;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 部门变更通知
 * @date 2021/11/20 13:14
 **/
@ApiModel
@Data
public class WeThirdBackDeptVo extends WeThirdBackBaseVo {

    @ApiModelProperty("部门Id")
    private String Id;

    @ApiModelProperty("部门名称")
    private String Name;

    @ApiModelProperty("父部门id")
    private String ParentId;

    @ApiModelProperty("部门排序")
    private String Order;

}
