package com.linkwechat.domain.kf.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author danmo
 * @description 新增客服入参
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeAddKfInfoQuery {

    private Long id;

    @NotBlank(message = "客服名称不能为空")
    @ApiModelProperty("客服名称")
    private String name;

    @NotBlank(message = "客服头像不能为空")
    @ApiModelProperty("客服头像")
    private String avatar;
}
