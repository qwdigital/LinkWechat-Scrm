package com.linkwechat.domain.shortlink.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @date 2023年01月09日 11:02
 */
@ApiModel
@Data
public class WeShortLinkStatisticsVo {

    @ApiModelProperty("访问总数(PV)")
    private Integer pvTotalCount = 0;

    @ApiModelProperty("访问总人数(UV)")
    private Integer uvTotalCount = 0;

    @ApiModelProperty("小程序打开总数")
    private Integer openTotalCount = 0;

    @ApiModelProperty("今日访问总数(PV)")
    private Integer pvTodayCount = 0;

    @ApiModelProperty("差异数(PV)")
    private Integer pvDiff = 0;

    @ApiModelProperty("今日访问总人数(UV)")
    private Integer uvTodayCount = 0;

    @ApiModelProperty("差异数(UV)")
    private Integer uvDiff = 0;

    @ApiModelProperty("今日小程序打开数")
    private Integer openTodayCount = 0;

    @ApiModelProperty("差异数(打开数)")
    private Integer openDiff = 0;

    @ApiModelProperty("时间横坐标")
    private List<String> xAxis;

    @ApiModelProperty("次数纵坐标")
    private JSONObject yAxis;


}
