package com.linkwechat.domain.wecom.query.customer.link;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;


/**
 * 获客助手获取链接参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeLinkCustomerQuery extends WeBaseQuery {

    //获客链接的id
    private String link_id;

    //更新的链接名称
    private String link_name;

    //是否无需验证，默认为true
    private boolean skip_verify;
    //范围
    private Range range;


    //range.user_list和range.department_list不可同时为空
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Range{

        //此获客链接关联的userid列表，最多可关联100个
        private String[] user_list;

        //此获客链接关联的部门id列表，部门覆盖总人数最多100个
        private Integer[] department_list;


    }


}
