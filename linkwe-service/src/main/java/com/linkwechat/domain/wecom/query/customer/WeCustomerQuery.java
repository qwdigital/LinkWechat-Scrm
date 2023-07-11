package com.linkwechat.domain.wecom.query.customer;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;

/**
 * @author danmo
 * @Description 获取客户列表入参
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCustomerQuery extends WeBaseQuery {
    /**
     * 外部联系人的userid
     */
    private String external_userid;

    /**
     * 上次请求返回的next_cursor
     */
    private String cursor;
}
