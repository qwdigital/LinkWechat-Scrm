package com.linkwechat.domain.operation.vo;

import lombok.Data;

/**
 * 群提醒数据分析
 */
@Data
public class WeGroupRemindAnalysisVo {

    //昨日客群总数
    int ydGroupTotalCnt;

    //昨日新增客群
    int ydGroupAddCnt;

    //昨日解散客群
    int ydGroupDissolveCnt;

    //昨日客群成员总数
    int ydMemberTotalCnt;

    //昨日新增客群成员
    int ydMemberAddCnt;

    //昨日退出客群成员
    int ydMemberQuitCnt;
}
