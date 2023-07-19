package com.linkwechat.domain.leads.record.query;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 线索跟进记录
 *
 * @author WangYX
 * @since 2023-04-07
 */
@Data
public class WeLeadsFollowRecordRequest {

    /**
     * 线索id
     */
    @NotNull(message = "线索id必填")
    private Long weLeadsId;

    /**
     * 跟进人id
     */
    @NotNull(message = "跟进人id必填")
    private Long followUserId;

}
