package com.linkwechat.domain.kf.query;

import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @Description 客服任务入参
 * @date 2021/12/13 10:57
 **/
@Data
public class WeKfMsgTaskQuery {

    /**
     * 企业Id
     */
    private String corpId;

    /**
     * 消息发送时间
     */
    private Long sendTime;

    /**
     * 客服帐号ID
     */
    private String openKfId;

    /**
     * 客户UserID
     */
    private String externalUserId;
}
