package com.linkwechat.domain.wecom.vo.customer.groupchat;

import com.linkwechat.domain.wecom.entity.customer.groupChat.WeGroupMemberEntity;
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
public class WeGroupChatDetailVo extends WeResultVo {
    private GroupChatDetail groupChat;


    @Data
    public static class GroupChatDetail {

        /**
         * 客户群ID
         */
        private String chatId;


        /**
         * 群名
         */
        private String name;

        /**
         * 群主ID
         */
        private String owner;

        /**
         * 群的创建时间
         */
        private Long createTime;

        /**
         * 群公告
         */
        private String notice;

        /**
         * 群成员列表
         */
        private List<WeGroupMemberEntity> memberList;

        /**
         * 群管理员列表
         */
        private List<WeGroupAdmin> adminList;
    }

    @Data
    public static class WeGroupAdmin {
        /**
         * 群管理员userid
         */
        private String userId;
    }
}
