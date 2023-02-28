package com.linkwechat.domain.know.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class WeKnowCustomerCountTrendOrTableVo {

    /**
     * 日期
     */
    @ExcelProperty("日期")
    private  String date;

    /**
     * 新客扫码人数
     */
    @ExcelProperty("新客扫码人数")
    private Long newCustomerScanNumber;

    /**
     * 添加客户人数
     */
    @ExcelProperty("添加客户人数")
    private Long addCustomerNumber;

    /**
     * 老客扫码人数
     */
    @ExcelProperty("老客扫码人数")
    private Long oldCustomerScanNumber;

    /**
     * 流失客户人数
     */
    @ExcelProperty("流失客户人数")
    private Long lostCustomerNumber;
}
