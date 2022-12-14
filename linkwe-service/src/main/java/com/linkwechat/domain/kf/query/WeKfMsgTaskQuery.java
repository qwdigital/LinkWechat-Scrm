package com.linkwechat.domain.kf.query;

import lombok.Data;

/**
 * @author danmo
 * @Description 客服任务入参
 * @date 2021/12/13 10:57
 **/
@Data
public class WeKfMsgTaskQuery {

    private Long poolId;

    /**
     * 企业Id
     */
    private String corpId;

    /**
     * 消息发送时间
     */
    private String sendTime;

    /**
     * 客服帐号ID
     */
    private String openKfId;

    /**
     * 客户UserID
     */
    private String externalUserId;
}
