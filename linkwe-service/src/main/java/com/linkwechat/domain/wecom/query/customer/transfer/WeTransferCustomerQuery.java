package com.linkwechat.domain.wecom.query.customer.transfer;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;

import java.util.List;

/**
 * @author danmo
 * @Description 分配在职成员的客户入参
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeTransferCustomerQuery extends WeBaseQuery {
    /**
     * 原跟进成员的userid
     */
    private String handover_userid;

    /**
     * 接替成员的userid
     */
    private String takeover_userid;

    /**
     * 客户的external_userid列表，每次最多分配100个客户
     */
    private List<String> external_userid;

    /**
     * 转移成功后发给客户的消息，最多200个字符，不填则使用默认文案
     */
    private String transfer_success_msg;

    private String cursor;
}
