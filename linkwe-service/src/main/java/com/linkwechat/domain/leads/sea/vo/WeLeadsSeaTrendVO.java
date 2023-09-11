package com.linkwechat.domain.leads.sea.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 分公海统计-数据趋势
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/17 14:06
 */
@Data
public class WeLeadsSeaTrendVO {
    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dateTime;
    /**
     * 当日领取量
     */
    private Long todayReceiveNum;
    /**
     * 当日有效跟进
     */
    private Long todayFollowNum;
}
