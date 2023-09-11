package com.linkwechat.domain.leads.leads.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

/**
 * 员工统计
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/19 18:33
 */
@Data
public class WeLeadsUserStatisticVO {

    /**
     * 跟进人Id
     */
    @ExcelIgnore
    private Long followId;

    /**
     * 跟进员工名称
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "员工名称", index = 0)
    private String userName;

    /**
     * 所属部门
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "所属部门", index = 1)
    private String deptName;

    /**
     * 线索转化率
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "线索转化率", index = 2)
    private String conversionRate = "0%";

    /**
     * 线索退回率
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "线索退回率", index = 3)
    private String returnRate = "0%";

    /**
     * 线索跟进人次
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "线索跟进人次", index = 4)
    private Integer followNum = 0;

    /**
     * 添加客户数
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "添加客户数", index = 5)
    private Integer customerNum = 0;
}
