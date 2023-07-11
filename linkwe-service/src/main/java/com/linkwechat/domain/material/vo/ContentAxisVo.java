package com.linkwechat.domain.material.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ContentAxisVo {
    @ApiModelProperty(value = "时间")
    private String dateStr;

    private Integer sendNum = 0;

    private Integer viewNum = 0;

    private Long viewByNum = 0L;
}
