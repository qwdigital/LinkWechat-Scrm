package com.linkwechat.domain.kf.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @date 2022年11月29日 11:20
 */
@ExcelIgnoreUnannotated
@ApiModel
@Data
public class WeKfQualityChatVo extends WeKfQualityBrokenLineVo{
    /**
     * 超时率
     */
    @ExcelProperty(value = "平均超时率",order = 6)
    @ApiModelProperty(value = "超时率")
    private String timeOutRate = "0%";


    /**
     * 超时时长
     */
    @ExcelProperty(value = "平均超时时长",order = 7)
    @ApiModelProperty(value = "超时时长")
    private String timeOutDuration = "0s";


    public void setLong2TimeOutDuration(Long outTimeDuration) {
        long hours = outTimeDuration / 3600;            //转换小时
        outTimeDuration = outTimeDuration % 3600;                //剩余秒数
        long minutes = outTimeDuration / 60;            //转换分钟
        outTimeDuration = outTimeDuration % 60;                //剩余秒数
        if (hours > 0) {
            this.timeOutDuration = hours + "h" + minutes + "m" + outTimeDuration + "s";
        } else {
            this.timeOutDuration = minutes + "m" + outTimeDuration + "s";
        }
    }
}
