package com.linkwechat.domain.wecom.query.customer.groupchat;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 获取客户群详情入参
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGroupChatDetailQuery extends WeBaseQuery {

    /**
     * 客户群ID
     */
    private String chat_id;

    /**
     * 是否需要返回群成员的名字group_chat.member_list.name。0-不返回；1-返回。默认不返回
     */
    private Integer need_name;

    public WeGroupChatDetailQuery() {
    }

    public WeGroupChatDetailQuery(String chatId, Integer needName) {
        this.chat_id = chatId;
        this.need_name = needName;
    }
}
