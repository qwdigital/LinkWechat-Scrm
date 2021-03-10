package com.linkwechat.wecom.domain.query;

import lombok.Data;

/**
 * @author danmo
 * @description
 * @date 2021/2/25 11:21
 **/
@Data
public class WePageStateQuery {
    /**
     * 日期间隔
     */
    private Integer few;

    /**
     * 日期
     */
    private String dateTime;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

}
