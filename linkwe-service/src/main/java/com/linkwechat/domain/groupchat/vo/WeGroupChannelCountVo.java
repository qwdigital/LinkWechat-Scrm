package com.linkwechat.domain.groupchat.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class WeGroupChannelCountVo {

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("客群总数")
    private Integer memberNumber;


    @ApiModelProperty("有效客群数")
    private Integer efficientNumber;
}