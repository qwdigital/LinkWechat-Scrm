package com.linkwechat.domain.storecode.vo.tab;

import lombok.Data;

/**
 * 门店导购统计头部tab相关数据
 */
@Data
public class WeStoreShopGuideTabVo {

    //点击扫码总次数
    private int totalShopGuideScannNumber;

    //今日点击扫码次数
    private int tdShopGuideScannNumber;

    //昨日点击扫码次数
    private int ydShopGuideScannNumber;

    //新增客户总数
    private int customerTotalNumber;

    //今日新增客户数
    private int ydCustomerNumber;

    //昨日新增客户数
    private int tdCustomerNumber;





}
