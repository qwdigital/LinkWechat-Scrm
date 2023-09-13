package com.linkwechat.domain.wecom.query.customer.link;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 获取获客链接列表入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeLinkCustomerListsQuery extends WeBaseQuery {

    /**
     * 返回的最大记录数，整型，最大值100
     */
    private int limit;
    private String cursor;
}

