package com.linkwechat.domain.strategic.crowd.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author danmo
 */
@ApiModel
@Data
public class WeCorpStateTagSourceQuery {

    @ApiModelProperty("code值")
    @NotNull(message = "code值不能为空")
    private Integer code;

    @ApiModelProperty("名称")
    private String name;
}
