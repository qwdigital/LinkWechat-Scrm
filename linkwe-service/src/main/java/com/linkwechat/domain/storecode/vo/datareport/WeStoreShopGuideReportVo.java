package com.linkwechat.domain.storecode.vo.datareport;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.linkwechat.common.annotation.Excel;
import lombok.Data;


/**
 * 门店导购码报表
 */
@Data
public class WeStoreShopGuideReportVo {
    //门店id
    @ExcelIgnore
    private Long storeCodeId;
    @ExcelProperty("日期")
    private String createTime;
    @ExcelProperty("所属地区")
    private String area;
    @ExcelProperty("门店名称")
    private String storeName;
    @ExcelProperty("点击/扫码总次数")
    private int totalScannNumber;
    @ExcelProperty("新增客户总数")
    private int customerTotalNumber;
    @ExcelProperty("今日点击/扫码总次数")
    private int tdScannNumber;
    @ExcelProperty("今日新增客户数")
    private int tdCustomerNumber;

}
