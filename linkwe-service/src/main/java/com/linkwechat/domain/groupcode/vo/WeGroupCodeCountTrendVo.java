package com.linkwechat.domain.groupcode.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 活码统计详情趋势
 */
@Data
public class WeGroupCodeCountTrendVo {
    //日期
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date dateTime;
    //离群员工总数
    private Integer exitChatGroupTotalMemberNum;
    //入群人工总数
    private Integer joinChatGroupTotalMemberNum;
}
