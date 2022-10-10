package com.linkwechat.domain.wecom.query.customer.transfer;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;

import java.util.List;

/**
 * @author danmo
 * @Description 分配离职成员的客户群入参
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeTransferGroupChatQuery extends WeBaseQuery {
    /**
     * 需要转群主的客户群ID列表。取值范围： 1 ~ 100
     */
    private List<String> chat_id_list;

    /**
     * 新群主ID
     */
    private String new_owner;

}
