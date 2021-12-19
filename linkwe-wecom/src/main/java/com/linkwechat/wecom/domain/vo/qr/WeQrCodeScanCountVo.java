package com.linkwechat.wecom.domain.vo.qr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 活码统计出参
 * @date 2021/11/11 0:36
 **/
@ApiModel
@Data
public class WeQrCodeScanCountVo {

    @ApiModelProperty("今日扫码次数")
    private Integer today = 0;

    @ApiModelProperty("累计扫码次数")
    private Integer total = 0;

    @ApiModelProperty("时间横坐标")
    private List<String> xAxis;

    @ApiModelProperty("扫码次数纵坐标")
    private List<Integer> yAxis;
}
