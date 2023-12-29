package com.linkwechat.domain.qr.vo;

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

    @ApiModelProperty("短链访问总数")
    private Integer linkVisitsTotal = 0;

    @ApiModelProperty("短链访问总人数")
    private Integer linkVisitsPeopleTotal = 0;

    @ApiModelProperty("短链今日访问总数")
    private Integer todayLinkVisitsTotal = 0;

    @ApiModelProperty("短链今日访问总人数")
    private Integer todayLinkVisitsPeopleTotal = 0;

    @ApiModelProperty("时间横坐标")
    private List<String> xAxis;

    @ApiModelProperty("扫码次数纵坐标")
    private List<Integer> yAxis;
}
