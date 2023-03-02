package com.linkwechat.domain.wecom.query.customer.groupchat;

import com.linkwechat.domain.wecom.entity.customer.groupChat.WeOwnerFilterEntity;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;

/**
 * @author danmo
 * @Description 获取客户群列表入参
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeGroupChatListQuery extends WeBaseQuery {
    /**
     * 客户群跟进状态过滤。
     * 0 - 所有列表(即不过滤)
     * 1 - 离职待继承
     * 2 - 离职继承中
     * 3 - 离职继承完成
     *
     * 默认为0
     */
    private Integer status_filter;

    /**
     * 群主过滤。
     * 如果不填，表示获取应用可见范围内全部群主的数据（但是不建议这么用，如果可见范围人数超过1000人，为了防止数据包过大，会报错 81017）
     */
    private WeOwnerFilterEntity owner_filter;

    /**
     * 用于分页查询的游标，字符串类型，由上一次调用返回，首次调用不填
     */
    private String cursor;

    /**
     * 分页，预期请求的数据量，取值范围 1 ~ 1000
     */
    private Integer limit = 500;
}
