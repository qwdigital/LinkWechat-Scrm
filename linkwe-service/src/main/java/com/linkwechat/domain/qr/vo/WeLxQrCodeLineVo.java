package com.linkwechat.domain.qr.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 活码统计出参
 * @date 2023/03/07 0:36
 **/
@ApiModel
@Data
public class WeLxQrCodeLineVo {

    @ApiModelProperty("今日新增新客数")
    private Integer today = 0;

    @ApiModelProperty("今日新增比昨日新客数")
    private Integer todayDiff = 0;

    @ApiModelProperty("累计新客数")
    private Integer total = 0;

    @ApiModelProperty("时间横坐标")
    private List<String> xAxis;

    @ApiModelProperty("扫码次数纵坐标")
    private List<Integer> yAxis;
}
