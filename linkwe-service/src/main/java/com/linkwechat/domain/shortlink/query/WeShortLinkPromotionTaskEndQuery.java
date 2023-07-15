package com.linkwechat.domain.shortlink.query;

import lombok.Data;

import java.util.Date;

/**
 * 任务结束
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/14 17:55
 */
@Data
public class WeShortLinkPromotionTaskEndQuery {

    /**
     * 短链推广Id
     */
    private Long promotionId;

    /**
     * 推广模板Id；
     */
    private Long businessId;

    /**
     * 推广方式：0群发客户，1群发客户群，2群发朋友圈，4应用消息
     */
    private Integer type;

    /**
     * 任务结束时间
     */
    private Date taskEndTime;
}
