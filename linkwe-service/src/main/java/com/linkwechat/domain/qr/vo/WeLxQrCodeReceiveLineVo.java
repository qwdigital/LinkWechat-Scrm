package com.linkwechat.domain.qr.vo;

import com.alibaba.fastjson.JSONObject;
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
public class WeLxQrCodeReceiveLineVo {

    @ApiModelProperty("时间横坐标")
    private List<String> xAxis;

    @ApiModelProperty("纵坐标")
    private List<String> yAxis;
}
