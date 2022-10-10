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
public class WeGroupChatStatisticQuery extends WeBaseQuery {
    /**
     * 起始日期的时间戳
     */
    private Long day_begin_time;

    /**
     * 结束日期的时间戳
     */
    private Long day_end_time;

    /**
     * 群主过滤
     */
    private OwnerFilter ownerFilter;

    @Data
    public static class OwnerFilter {
        /**
         * 群主ID列表
         */
        private List<String> userid_list;
    }
}
