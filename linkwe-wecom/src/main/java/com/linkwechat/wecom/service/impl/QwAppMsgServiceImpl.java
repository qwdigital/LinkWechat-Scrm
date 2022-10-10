package com.linkwechat.wecom.service.impl;

import com.linkwechat.domain.wecom.query.msg.WeAppMsgQuery;
import com.linkwechat.domain.wecom.vo.msg.WeAppMsgVo;
import com.linkwechat.wecom.client.WeAppMsgClient;
import com.linkwechat.wecom.service.IQwAppMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 消息
 * @date 2022/4/14 23:35
 **/
@Slf4j
@Service
public class QwAppMsgServiceImpl implements IQwAppMsgService {

    @Autowired
    private WeAppMsgClient weAppMsgClient;

    @Override
    public WeAppMsgVo sendAppMsg(WeAppMsgQuery query) {
        return weAppMsgClient.sendAppMsg(query);
    }
}
