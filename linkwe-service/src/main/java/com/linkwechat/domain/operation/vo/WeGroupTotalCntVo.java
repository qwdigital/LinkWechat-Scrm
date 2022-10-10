package com.linkwechat.domain.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客群总数
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeGroupTotalCntVo {

    @ApiModelProperty("日期")
    private String xTime;

    @ApiModelProperty("总数")
    private Integer totalCnt;

}
