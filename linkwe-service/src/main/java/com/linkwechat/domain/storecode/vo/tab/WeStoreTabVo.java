package com.linkwechat.domain.storecode.vo.tab;

import lombok.Data;

/**
 * 门店统计头部tab
 */
@Data
public class WeStoreTabVo {
    //点击扫码总次数
    private int totalShopGuideScannNumber;

    //今日点击扫码次数
    private int tdShopGuideScannNumber;

    //新增客户总数
    private int customerTotalNumber;

    //今日新增客户数
    private int ydCustomerNumber;


    //门店群活码扫码总次数
    private int totalStoreGroupScannNumber;

    //今日门店群活码扫码总次数
    private int tdStoreGroupScannNumber;

    //扫码进群总次数
    private int totalJoinGroupMemberNumber;

    //今日扫码进群总次数
    private int tdJoinGroupMemberNumber;


    //离群总次数
    private int totalExitGroupMemberNumber;


    //今日离群总次数
    private int tdExitGroupMemberNumber;



}
