package com.linkwechat.domain.wecom.vo.customer.groupchat;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 获取客户群列表
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGroupChatListVo extends WeResultVo {
    /**
     * 客户群列表
     */
    private List<GroupChat> groupChatList;

    @Data
    public static class GroupChat {
        /**
         * 客户群ID
         */
        private String chatId;

        /**
         * 客户群跟进状态。
         * 0 - 跟进人正常
         * 1 - 跟进人离职
         * 2 - 离职继承中
         * 3 - 离职继承完成
         */
        private Integer status;
    }
}
