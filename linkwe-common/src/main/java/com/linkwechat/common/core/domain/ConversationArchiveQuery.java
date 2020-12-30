package com.linkwechat.common.core.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author sxw
 * @description 会话存档接口入参实体
 * @date 2020/12/29 14:23
 **/
@Data
public class ConversationArchiveQuery extends BaseEntity {
    /** 发送人Id */
    private String fromId;

    /** 接收人Id */
    private String receiveId;

    /** 群聊Id */
    private String roomId;

    /** 类型 */
    private String msgType;

    @JSONField(defaultValue = "1")
    private Integer pageSize;

    @JSONField(defaultValue = "10")
    private Integer pageNum;
}
