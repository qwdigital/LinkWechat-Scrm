package com.linkwechat.domain.wecom.entity.customer.groupChat;

import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description
 * @date 2021/12/2 11:06
 **/
@Data
public class WeOwnerFilterEntity {

    /**
     * 用户ID列表。最多100个
     */
    private List<String> userid_list;
}
