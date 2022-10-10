package com.linkwechat.domain.storecode.vo.trend;

import lombok.Data;

/**
 * 门店群码统计趋势
 */
@Data
public class WeStoreGroupTrendVo {

    //日期
    private String dataTime;
    //点击扫码总次数
    private int totalStoreGroupScannNumber;
    //进群客户数
    private int totalJoinGroupMemberNumber;
    //退群客户数
    private int totalExitGroupMemberNumber;


}
