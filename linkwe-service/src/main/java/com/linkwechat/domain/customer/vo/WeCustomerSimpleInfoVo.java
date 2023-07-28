package com.linkwechat.domain.customer.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 客户简单详情
 */
@ApiModel
@Data
public class WeCustomerSimpleInfoVo {

    //客户名称
    @ApiModelProperty("客户名称")
    private String customerName;

    //头像
    @ApiModelProperty("头像")
    private String avatar;
    //客户
    @ApiModelProperty("客户externalUserId")
    private String externalUserid;
}

