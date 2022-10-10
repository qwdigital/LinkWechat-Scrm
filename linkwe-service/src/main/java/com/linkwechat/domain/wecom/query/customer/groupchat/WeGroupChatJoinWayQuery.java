package com.linkwechat.domain.wecom.query.customer.groupchat;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;

import java.util.List;

/**
 * 获取客户群进群方式配置参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeGroupChatJoinWayQuery extends WeBaseQuery {
    //联系方式的配置id
    private String config_id;
}
