package com.linkwechat.wecom.service;

import com.linkwechat.domain.wecom.query.msg.WeAppMsgQuery;
import com.linkwechat.domain.wecom.vo.msg.WeAppMsgVo;

/**
 * @author sxw
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
}
