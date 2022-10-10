package com.linkwechat.domain.wecom.vo.customer.msg;

import lombok.Data;

/**
 * @author danmo
 * @Description 群成员发送结果列表
 * @date 2021/10/3 17:02
 **/
@Data
public class WeGroupMsgSendVo {

    /**
     * 外部联系人userid，群发消息到企业的客户群不返回该字段
     */
    private String externalUserId;

    /**
     * 外部客户群id，群发消息到客户不返回该字段
     */
    private String chatId;

    /**
     * 企业服务人员的userid
     */
    private String userId;

    /**
     * 发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
     */
    private Integer status;

    /**
     * 发送时间，发送状态为1时返回
     */
    private Long sendTime;
}
