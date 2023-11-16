package com.linkwechat.domain.groupcode.vo;

import lombok.Data;


/**
 * 活码群统计相关
 */
@Data
public class WeGroupChatInfoVo {
    /**
     * 群总人数
     */
    private Integer chatGroupMemberTotalNum;


    /**
     * 进群人数
     */
    private Integer joinChatGroupTotalMemberNum;


    /**
     * 退群人数
     */
    private Integer exitChatGroupTotalMemberNum;

    /**
     * 昨日以前群总人数
     */
    private Integer oldChatGroupMemberTotalNum;


    /**
     * 昨日以前进群人数
     */
    private Integer oldJoinChatGroupTotalMemberNum;


    /**
     * 今日以前进群人数
     */
    private Integer newJoinChatGroupTotalMemberNum;


    /**
     * 昨日以前退群人数
     */
    private Integer oldExitChatGroupTotalMemberNum;

    /**
     * 群名
     */
    private String groupName;


    /**
     * 群id
     */
    private String chatId;


    /**
     * 群主名
     */
    private String groupOwner;

    /**
     * 关联状态 0-未关联 1-关联
     */
    private Integer status;


}
