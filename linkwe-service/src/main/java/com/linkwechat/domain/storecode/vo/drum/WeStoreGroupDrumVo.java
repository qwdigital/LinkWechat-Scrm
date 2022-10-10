package com.linkwechat.domain.storecode.vo.drum;

import lombok.Data;

/**
 * 门店群柱状图
 */
@Data
public class WeStoreGroupDrumVo {
    //门店名
    private String storeName;

    //新增客户数
    private Integer customerNumber;
}
