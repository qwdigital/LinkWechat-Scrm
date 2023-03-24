package com.linkwechat.domain.qr.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @description 活码统计出参
 * @date 2023/03/07 0:36
 **/
@ApiModel
@Data
public class WeLxQrCodeSheetVo {


    @ApiModelProperty("日期")
    @ExcelProperty("日期")
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date dateTime;

    @ApiModelProperty("今日新增新客数")
    @ExcelProperty("今日新增新客数")
    private Integer todayNum = 0;

    @ApiModelProperty("新增总新客数")
    @ExcelProperty("新增总新客数")
    private Integer totalNum = 0;
}
