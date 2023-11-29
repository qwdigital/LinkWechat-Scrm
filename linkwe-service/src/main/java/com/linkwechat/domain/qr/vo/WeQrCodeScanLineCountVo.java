package com.linkwechat.domain.qr.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 活码统计出参
 * @date 2021/11/11 0:36
 **/
@ExcelIgnoreUnannotated
@ApiModel
@Data
public class WeQrCodeScanLineCountVo {

    @ExcelProperty(value = "今日扫码数",order = 2)
    @ApiModelProperty("今日扫码次数")
    private Integer today = 0;

    @ExcelProperty(value = "累计扫码总数",order = 1)
    @ApiModelProperty("累计扫码次数")
    private Integer total = 0;

    @ExcelProperty(value = "短链访问总数",order = 3)
    @ApiModelProperty("短链访问总数")
    private Integer linkVisitsTotal = 0;

    //@ExcelProperty(value = "短链访问总人数",order = 4)
    @ApiModelProperty("短链访问总人数")
    private Integer linkVisitsPeopleTotal = 0;

    @ExcelProperty(value = "短链今日访问总数",order = 5)
    @ApiModelProperty("短链今日访问总数")
    private Integer todayLinkVisitsTotal = 0;

    //@ExcelProperty(value = "短链今日访问总人数",order = 6)
    @ApiModelProperty("短链今日访问总人数")
    private Integer todayLinkVisitsPeopleTotal = 0;

    @ExcelProperty(value = "日期",order = 0)
    @ApiModelProperty("日期")
    private String dateTime;
}
