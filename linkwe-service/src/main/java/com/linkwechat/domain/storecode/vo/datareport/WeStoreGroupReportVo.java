package com.linkwechat.domain.storecode.vo.datareport;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.linkwechat.common.annotation.Excel;
import lombok.Data;

/**
 * 门店群活码统计
 */
@Data
public class WeStoreGroupReportVo {
    @ExcelIgnore
    private Long storeCodeId;
    @ExcelProperty("日期")
    private String createTime;
    @ExcelProperty("所属地区")
    private String area;
    @ExcelIgnore
    private String address;
    @ExcelProperty("门店名称")
    private String storeName;
    @ExcelProperty("点击/扫码总次数")
    private int totalStoreGroupScannNumber;
    @ExcelProperty("进群总客户数")
    private int totalJoinGroupMemberNumber;
    @ExcelProperty("退群总客户数")
    private int totalExitGroupMemberNumber;
    @ExcelProperty("今日点击/扫码总次数")
    private int tdStoreGroupScannNumber;
    @ExcelProperty("今日进群客户数")
    private int tdJoinGroupMemberNumber;
    @ExcelProperty("今日退群客户数")
    private int tdExitGroupMemberNumber;


}
