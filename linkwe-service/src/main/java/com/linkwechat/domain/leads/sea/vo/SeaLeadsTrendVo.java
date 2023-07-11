package com.linkwechat.domain.leads.sea.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author caleb
 * @since 2023/5/8
 */
@Data
public class SeaLeadsTrendVo {
    //日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dateTime;
    // 当日领取量
    private Integer todayReceiveNum;
    // 当日有效跟进
    private Integer todayFollowNum;
}
