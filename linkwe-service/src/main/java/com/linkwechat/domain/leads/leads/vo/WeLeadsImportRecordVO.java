package com.linkwechat.domain.leads.leads.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 线索导入记录
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/19 16:39
 */
@Data
public class WeLeadsImportRecordVO {

    /**
     * 主键Id
     */
    @ExcelIgnore
    private Long id;

    /**
     * 公海Id
     */
    @ExcelIgnore
    private Long seaId;

    /**
     * 公海名称
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "所属公海", index = 3)
    private String seaName;

    /**
     * 导入文件名称
     */
    @ExcelProperty(value = "导入来源", index = 0)
    @ColumnWidth(35)
    private String importSourceFileName;

    /**
     * 导入线索总数
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "导入线索总数", index = 1)
    private Integer num;

    /**
     * 导入时间
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "导入时间", index = 2)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date importTime;

    /**
     * 线索转化率
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "线索转化率", index = 4)
    private String conversionRate = "0%";

    /**
     * 线索退回率
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "线索退回率", index = 5)
    private String returnRate = "0%";

    /**
     * 线索跟进人次
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "线索跟进人次", index = 6)
    private Integer followNum = 0;

    /**
     * 添加客户数
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "添加客户数", index = 7)
    private Integer customerNum = 0;

}
