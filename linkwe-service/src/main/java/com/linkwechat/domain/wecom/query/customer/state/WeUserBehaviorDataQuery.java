package com.linkwechat.domain.wecom.query.customer.state;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeUserBehaviorDataQuery extends WeBaseQuery {

    /**
     * 成员ID列表，最多100个
     */
    private List<String> userid;

    /**
     * 部门ID列表，最多100个
     */
    private List<String> partyid;

    /**
     * 数据起始时间
     */
    private Long start_time;

    /**
     * 数据结束时间
     */
    private Long end_time;
}
