package com.linkwechat.domain.wecom.callback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class WeBackKfVo extends WeBackBaseVo{

    @ApiModelProperty("客服ID")
    private String OpenKfId;
}
