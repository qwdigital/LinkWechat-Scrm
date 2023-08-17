package com.linkwechat.domain.leads.sea.vo;

import lombok.Data;

/**
 * 公海统计
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/08/17 16:31
 */
@Data
public class WeLeadsSeaStatisticVO {

    /**
     * 日期
     */
    private String dateTime;

    /**
     * 总领取量
     */
    private Integer allReceiveNum;

    /**
     * 总跟进量
     */
    private Integer allFollowerNum;

    /**
     * 当日领取量
     */
    private Integer todayReceiveNum;

    /**
     * 当日跟进量
     */
    private Integer todayFollowNum;

    /**
     * 当日跟进率
     */
    private Integer todayFollowRatio;

}
