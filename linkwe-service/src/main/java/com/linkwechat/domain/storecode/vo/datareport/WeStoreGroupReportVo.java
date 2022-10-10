package com.linkwechat.domain.storecode.vo.datareport;

import com.linkwechat.common.annotation.Excel;
import lombok.Data;

/**
 * 门店群活码统计
 */
@Data
public class WeStoreGroupReportVo {
    private Long storeCodeId;
    @Excel(name="日期")
    private String createTime;
    @Excel(name = "所属地区")
    private String area;
    private String address;
    @Excel(name="门店名称")
    private String storeName;
    @Excel(name="点击/扫码总次数")
    private int totalStoreGroupScannNumber;
    @Excel(name="进群总客户数")
    private int totalJoinGroupMemberNumber;
    @Excel(name="退群总客户数")
    private int totalExitGroupMemberNumber;
    @Excel(name="今日点击/扫码总次数")
    private int tdStoreGroupScannNumber;
    @Excel(name="今日进群客户数")
    private int tdJoinGroupMemberNumber;
    @Excel(name="今日退群客户数")
    private int tdExitGroupMemberNumber;


}
