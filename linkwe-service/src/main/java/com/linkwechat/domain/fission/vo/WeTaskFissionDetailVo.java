package com.linkwechat.domain.fission.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class WeTaskFissionDetailVo {
    /**
     *明细id
     */
    @ExcelIgnore
    private Long fissionInviterRecordId;

    /**
     * 老客
     */
    @ExcelProperty("老客")
    private String oldCustomerName;

    /**
     *发送员工
     */
    @ExcelProperty("发送员工")
    private String sendWeUserName;


    /**
     * 裂变状态
     */
    @ExcelProperty("裂变状态")
    private Integer inviterState;


    /**
     * 裂变新客数
     */
    @ExcelProperty("裂变新客数")
    private Integer inviterNumber;

}
