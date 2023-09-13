package com.linkwechat.domain.leads.record.query;

import lombok.Data;

/**
 * 线索跟进记录
 *
 * @author WangYX
 * @since 2023-04-07
 */
@Data
public class WeLeadsFollowRecordRequest {

    /**
     * 线索跟进人表id
     */
    private Long id;

    /**
     * 线索id
     */
    private Long weLeadsId;

    /**
     * 跟进人id
     */
    private Long followUserId;

}
