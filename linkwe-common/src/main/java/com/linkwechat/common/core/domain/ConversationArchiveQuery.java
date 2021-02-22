package com.linkwechat.common.core.domain;

import lombok.Data;

/**
 * @author danmo
 * @description 会话存档接口入参实体
 * @date 2020/12/29 14:23
 **/
@Data
public class ConversationArchiveQuery extends BaseEntity {
    /** 发送人Id */
    private String fromId;

    /** 成员名称 */
    private String userName;

    /** 接收人Id */
    private String receiveId;

    /** 客户姓名 */
    private String customerName;

    /** 群聊Id */
    private String roomId;

    /** 类型 */
    private String msgType;

    /** 关键词 **/
    private String keyWord;

    /** 消息动作 */
    private String action;
}
