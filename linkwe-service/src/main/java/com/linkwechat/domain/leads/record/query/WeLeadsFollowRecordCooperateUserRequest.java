package com.linkwechat.domain.leads.record.query;

import lombok.Data;

/**
 * 跟进记录内容协作成员
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 15:51
 */
@Data
public class WeLeadsFollowRecordCooperateUserRequest {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户企微Id
     */
    private String weUserId;

    /**
     * 用户名称
     */
    private String userName;
}
