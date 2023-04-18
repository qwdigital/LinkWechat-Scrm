package com.linkwechat.domain.fission.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class WeFissionDataReportVo {

    /**
     * 日期
     */
    @ExcelProperty("日期")
    private String date;


    /**
     * 完成任务老客总数
     */
    @ExcelProperty("完成任务老客总数")
    private Integer completeTaskOldCustomerNum;

    /**
     * 裂变新客总数
     */
    @ExcelProperty("裂变新客总数")
    private Integer fissionCustomerNum;

    /**
     * 今日完成任务老客数
     */
    @ExcelProperty("今日完成任务老客数")
    private Integer tdCompleteTaskOldCustomerNum;

    /**
     * 今日裂变新客数
     */
    @ExcelProperty("今日裂变新客数")
    private Integer tdFissionCustomerNum;
}
