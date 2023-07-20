package com.linkwechat.domain.message.query;

import com.linkwechat.common.enums.message.MessageTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 线索中心-消息通知mq
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/11 16:53
 */
@Data
public class WeClientLeadsNewsMqRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 线索Id
     */
    private Long leadsId;

    /**
     * 企微员工Id
     */
    private String weUserIds;

    /**
     * 线索转移Id
     */
    private Long transferId;

    /**
     * 消息类型枚举
     */
    private MessageTypeEnum type;

}
