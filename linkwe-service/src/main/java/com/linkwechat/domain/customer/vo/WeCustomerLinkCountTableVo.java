package com.linkwechat.domain.customer.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class WeCustomerLinkCountTableVo {


    /**
     * 时间
     */
    @ExcelProperty("时间")
    private String date;
    /**
     * 新增客户数
     */
    @ExcelProperty("新增客户数")
    private int addWeCustomerNumber;

    /**
     * 活跃客户数
     */
    @ExcelProperty("活跃客户数")
    private int weCustomerActiveNumber;


    /**
     * 今日新增客户数
     */
    @ExcelProperty("今日新增客户数")
    private int tdAddWeCustomerNumber;

    /**
     * 今日活跃客户数
     */
    @ExcelProperty("今日活跃客户数")
    private int tdWeCustomerActiveNumber;
}
