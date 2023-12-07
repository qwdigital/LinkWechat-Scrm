package com.linkwechat.domain.wecom.query.customer;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author danmo
 * @Description 获取客户列表入参
 * @date 2021/12/2 16:11
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCustomerListQuery extends WeBaseQuery {
    /**
     * 企业成员的userid
     */
    private String userid;
}
