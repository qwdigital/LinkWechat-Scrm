package com.linkwechat.domain.wecom.query.customer.msg;

import lombok.Data;

/**
 * 停止企业群发
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/14 17:15
 */
@Data
public class WeCancelGroupMsgSendQuery {

    /**
     * 群发消息的id
     */
    private String msgid;

}
