package com.linkwechat.domain.wecom.query.customer.link;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;


/**
 * 获取由获客链接添加的客户信息参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeLinkCustomerCountQuery extends WeBaseQuery {

    //获客链接id
    private String link_id;

   //返回的最大记录数，整型，最大值1000
    private Integer limit;

   //用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填
    private String cursor;

}
