package com.linkwechat.domain.fission.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class WeFissionDetailVo {
    /**
     *明细id
     */
    @ExcelIgnore
    private Long fissionDetailId;

    /**
     * 老客
     */
    @ExcelProperty("老客")
    private String oldCustomerName;

    /**
     * 发送员工
     */
    @ExcelProperty("发送员工")
    private String sendUserName;

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

    /**
     * 成员id，如果为群裂变此处为群成员id，如果是任务宝，此处为客户外部联系人id
     */
    @ExcelIgnore
    private String inviterMemberId;
}
