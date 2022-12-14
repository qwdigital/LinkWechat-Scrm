package com.linkwechat.domain.kf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @date 2022年11月21日 13:59
 */
@Data
@ApiModel
public class WeKfMsgAnalyzeVo extends WeKfEvaluationVo {

    @ApiModelProperty("超时提醒次数")
    private Integer outTimeNoticeCnt;

    @ApiModelProperty("超时回复时长")
    private String outTimeDuration;

    @ApiModelProperty("会话超时率")
    private String outTimeRate = "0";


    public void setOutTimeDuration(Long outTimeDuration) {
        long hours = outTimeDuration / 3600;            //转换小时
        outTimeDuration = outTimeDuration % 3600;                //剩余秒数
        long minutes = outTimeDuration / 60;            //转换分钟
        outTimeDuration = outTimeDuration % 60;                //剩余秒数
        if (hours > 0) {
            this.outTimeDuration = hours + "h" + minutes + "m" + outTimeDuration + "s";
        } else {
            this.outTimeDuration = minutes + "m" + outTimeDuration + "s";
        }
    }
}
