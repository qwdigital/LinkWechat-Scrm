package com.linkwechat.domain.kf.query;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 客服列表入参
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeKfListQuery extends BaseEntity {

    @ApiModelProperty("客服名称")
    private String name;

    @ApiModelProperty("接待方式: 1-人工客服 2-智能助手")
    private Integer receptionType;

    @ApiModelProperty("接待员工id")
    private List<String> userIds;

    @ApiModelProperty("接待场景id")
    private List<String> scenesIds;

    @ApiModelProperty(hidden = true)
    private List<Long> ids;
}
