package com.linkwechat.domain.qr.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 活码员工排期入参
 * @date 2021/11/7 13:58
 **/
@ApiModel
@Data
public class WeQrUserInfoDetailQuery {

    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    private String userId;

    /**
     * 排班次数
     */
    @ApiModelProperty(value = "排班次数")
    private Integer schedulingNum;

}
