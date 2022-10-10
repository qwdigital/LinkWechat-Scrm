package com.linkwechat.domain.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客户总数
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeCustomerTotalCntVo {

    @ApiModelProperty("日期")
    private String xTime;

    @ApiModelProperty("客户总数")
    private Integer totalCnt;

}
