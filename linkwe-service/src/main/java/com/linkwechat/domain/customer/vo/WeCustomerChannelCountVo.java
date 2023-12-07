package com.linkwechat.domain.customer.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class WeCustomerChannelCountVo {

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("客户总数")
    private Integer customerNumber;

    @ApiModelProperty("有效客户数")
    private Integer efficientNumber;
}
