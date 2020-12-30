package com.linkwechat.wecom.domain.dto.msgaudit;

import lombok.Data;

/**
 * @author sxw
 * @description
 * @date 2020/12/30 23:41
 **/
@Data
public class GroupChatVo {
    /**客户群ID*/
    private String chat_id;

    /**群名*/
    private String name;

    /**群主ID*/
    private String owner;

    /**群的创建时间*/
    private long create_time;

    /**群公告*/
    private String notice;
}
