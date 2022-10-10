package com.linkwechat.domain.wecom.vo.kf;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

/**
 * @author danmo
 * @Description 客服
 * @date 2021/12/13 10:57
 **/
@Data
public class WeKfMsgVo extends WeResultVo {

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 客服帐号ID
     */
    private String openKfId;

    /**
     * 客户UserID
     */
    private String externalUserId;

    /**
     * 消息发送时间
     */
    private String sendTime;

    /**
     * 消息来源。3-微信客户发送的消息 4-系统推送的事件消息 5-接待人员在企业微信客户端发送的消息
     */
    private String origin;

    /**
     * 从企业微信给客户发消息的客服人员userid
     */
    private String servicerUserId;

    /**
     * 对不同的msgtype，有相应的结构描述
     */
    private String msgType;
}
