package com.linkwechat.domain.storecode.vo.datareport;

import com.linkwechat.common.annotation.Excel;
import lombok.Data;


/**
 * 门店导购码报表
 */
@Data
public class WeStoreShopGuideReportVo {
    //门店id
    private Long storeCodeId;
    @Excel(name = "日期")
    private String createTime;
    @Excel(name = "所属地区")
    private String area;
    @Excel(name = "门店名称")
    private String storeName;
    @Excel(name = "点击/扫码总次数")
    private int totalScannNumber;
    @Excel(name = "新增客户总数")
    private int customerTotalNumber;
    @Excel(name = "今日点击/扫码总次数")
    private int tdScannNumber;
    @Excel(name = "今日新增客户数")
    private int tdCustomerNumber;

}
