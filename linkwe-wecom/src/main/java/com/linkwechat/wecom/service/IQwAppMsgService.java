package com.linkwechat.wecom.service;

import com.linkwechat.domain.wecom.query.msg.WeAppMsgQuery;
import com.linkwechat.domain.wecom.query.msg.WeRecallMsgQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.msg.WeAppMsgVo;

/**
 * @author danmo
 * @description 消息
 * @date 2022/4/14 23:35
 **/
public interface IQwAppMsgService {

    /**
     * 发送应用消息
     * @param query
     * @return
     */
    WeAppMsgVo sendAppMsg(WeAppMsgQuery query);

    WeResultVo recallAppMsg(WeRecallMsgQuery query);

    WeResultVo recallAgentAppMsg(WeRecallMsgQuery query);
}
