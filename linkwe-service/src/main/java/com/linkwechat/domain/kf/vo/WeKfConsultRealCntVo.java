package com.linkwechat.domain.kf.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.NumberFormat;

/**
 * @author danmo
 * @description 客服客户场景实时数据
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeKfConsultRealCntVo {

    @ApiModelProperty("时间")
    @ExcelProperty("日期")
    private String xTime;

    @ApiModelProperty("咨询客户总数")
    @ExcelProperty("咨询客户总数")
    private Integer consultTotalCnt;

    @ApiModelProperty("接待客户总数")
    @ExcelProperty("接待客户总数")
    private Integer receptionTotalCnt;

    @ApiModelProperty("回复客户总数")
    @ExcelProperty("回复客户总数")
    private Integer replyTotalCnt;

    @ApiModelProperty("回复聊天占比")
    @ExcelProperty("回复聊天占比")
    private String replyProportionCnt;

    @ApiModelProperty("平均咨询响应时长")
    @ExcelProperty("平均咨询响应时长")
    private String avgConsultReplyDuration = "0";

    @ApiModelProperty("平均咨询时长")
    @ExcelProperty("平均咨询时长")
    private String avgConsultDuration = "0";

    public String getReplyProportionCnt() {
        if(replyTotalCnt == null || replyTotalCnt == 0){
            return "0%";
        }
        if(consultTotalCnt == null){
            return "-";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format((float)replyTotalCnt/(float)consultTotalCnt*100) + "%";
    }

}
