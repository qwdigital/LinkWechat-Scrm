package com.linkwechat.domain.strategic.crowd.query;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author danmo
 * @description 策略人群列表入参
 * @date 2021/11/7 13:49
 **/
@ApiModel
@Data
public class WeStrategicCrowdListQuery extends BaseEntity {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("分组ID")
    @NotNull(message = "分组ID不能为空")
    private Long groupId;

    @ApiModelProperty("更新方式 1：手动 2：自动")
    private Integer type = 1;

    @ApiModelProperty(value = "状态 1、待计算 2、计算中 3、计算完成 4、计算失败")
    private Integer status;
}
