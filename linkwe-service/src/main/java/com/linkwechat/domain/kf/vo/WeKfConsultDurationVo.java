package com.linkwechat.domain.kf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 咨询客户分析
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeKfConsultDurationVo {


    @ApiModelProperty("时间")
    private String xTime;

    @ApiModelProperty("平均咨询响应时长")
    private String avgConsultReplyDuration = "0";

    @ApiModelProperty("平均咨询时长")
    private String avgConsultDuration = "0";


    public String getAvgConsultReplyDuration() {
        long second = Math.round(Double.parseDouble(avgConsultReplyDuration));
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second / 60;            //转换分钟
        second = second % 60;                //剩余秒数
        if (hours > 0) {
            return hours + "h" + minutes + "m" + second + "s";
        } else {
            return minutes + "m" + second + "s";
        }
    }

    public String getAvgConsultDuration() {
        long second = Math.round(Double.parseDouble(avgConsultDuration));
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second / 60;            //转换分钟
        second = second % 60;                //剩余秒数
        if (hours > 0) {
            return hours + "h" + minutes + "m" + second + "s";
        } else {
            return minutes + "m" + second + "s";
        }
    }
}
