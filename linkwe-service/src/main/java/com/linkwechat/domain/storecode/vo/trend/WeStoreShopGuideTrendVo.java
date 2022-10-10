package com.linkwechat.domain.storecode.vo.trend;

import lombok.Data;

/**
 * 导购趋势图
 */
@Data
public class WeStoreShopGuideTrendVo {
    //日期
    private String dataTime;
    //点击导购码扫码总次数
    private int totalShopGuideScannNumber;
    //新增客户数
    private int customerTotalNumber;
}
