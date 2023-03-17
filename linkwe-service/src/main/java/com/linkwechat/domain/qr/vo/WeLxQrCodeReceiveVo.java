package com.linkwechat.domain.qr.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 活码统计出参
 * @date 2023/03/07 0:36
 **/
@ApiModel
@Data
public class WeLxQrCodeReceiveVo {

    @ApiModelProperty("红包领取总数")
    private Integer totalNum = 0;

    @ApiModelProperty("红包领取总金额(元)")
    private String totalAmount = "0";

    @ApiModelProperty("今日红包领取数")
    private Integer todayNum = 0;

    @ApiModelProperty("今日红包领取数差值")
    private Integer todayNumDiff = 0;

    @ApiModelProperty("今日红包领取金额(元)")
    private String todayAmount = "0";

    @ApiModelProperty("今日红包领取金额差值")
    private String todayAmountDiff = "0";
}
