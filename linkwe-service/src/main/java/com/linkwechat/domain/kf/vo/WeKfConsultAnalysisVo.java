package com.linkwechat.domain.kf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.NumberFormat;

/**
 * @author danmo
 * @description 咨询客户分析
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeKfConsultAnalysisVo {

    @ApiModelProperty("咨询客户总数")
    private Integer consultCnt;

    @ApiModelProperty("接待客户总数")
    private Integer receptionCnt;

    @ApiModelProperty("回复客户总数")
    private Integer replyCnt;

    @ApiModelProperty("回复聊天占比")
    private String replyProportionCnt;

    @ApiModelProperty("平均咨询响应时长")
    private String avgConsultReplyDuration;

    @ApiModelProperty("平均咨询时长")
    private String avgConsultDuration;

    public String getReplyProportionCnt() {
        if(replyCnt == 0){
            return "0%";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format((float)replyCnt/(float)consultCnt*100) + "%";
    }

    public String getAvgConsultReplyDuration() {
        long second = Math.round(Double.parseDouble(avgConsultReplyDuration));
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second /60;            //转换分钟
        second = second % 60;                //剩余秒数
        if(hours>0){
            return hours + "h" + minutes + "m" + second + "s";
        }else{
            return minutes + "m" + second + "s";
        }
    }

    public String getAvgConsultDuration() {
        long second = Math.round(Double.parseDouble(avgConsultDuration));
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second /60;            //转换分钟
        second = second % 60;                //剩余秒数
        if(hours>0){
            return hours + "h" + minutes + "m" + second + "s";
        }else{
            return minutes + "m" + second + "s";
        }
    }
}
