package com.linkwechat.domain.wecom.entity.customer.groupChat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author danmo
 * @Description
 * @date 2021/12/2 11:06
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeOwnerFilterEntity {

    /**
     * 用户ID列表。最多100个
     */
    private List<String> userid_list;
}
