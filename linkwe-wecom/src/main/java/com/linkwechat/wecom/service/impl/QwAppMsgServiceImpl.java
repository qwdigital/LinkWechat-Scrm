package com.linkwechat.wecom.service.impl;

import com.linkwechat.domain.wecom.query.msg.WeAppMsgQuery;
import com.linkwechat.domain.wecom.query.msg.WeRecallMsgQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.msg.WeAppMsgVo;
import com.linkwechat.wecom.client.WeAppMsgClient;
import com.linkwechat.wecom.service.IQwAppMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author danmo
 * @description 消息
 * @date 2022/4/14 23:35
 **/
@Slf4j
@Service
public class QwAppMsgServiceImpl implements IQwAppMsgService {

    @Resource
    private WeAppMsgClient weAppMsgClient;

    @Override
    public WeAppMsgVo sendAppMsg(WeAppMsgQuery query) {
        return weAppMsgClient.sendAppMsg(query);
    }

    @Override
    public WeResultVo recallAppMsg(WeRecallMsgQuery query) {
        return weAppMsgClient.recallMsg(query);
    }

    @Override
    public WeResultVo recallAgentAppMsg(WeRecallMsgQuery query) {
        return weAppMsgClient.recallAgentMsg(query);
    }
}
