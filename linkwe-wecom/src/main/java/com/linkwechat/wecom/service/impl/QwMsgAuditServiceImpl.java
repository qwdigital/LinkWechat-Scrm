package com.linkwechat.wecom.service.impl;

import com.linkwechat.domain.wecom.query.msgaudit.WeMsgAuditQuery;
import com.linkwechat.domain.wecom.vo.msgaudit.WeMsgAuditVo;
import com.linkwechat.wecom.client.WeMsgAuditClient;
import com.linkwechat.wecom.service.IQwMsgAuditService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 会话存档业务实现类
 * @author danmo
 * @date 2023年05月12日 10:01
 */
@Service
public class QwMsgAuditServiceImpl implements IQwMsgAuditService {

    @Resource
    private WeMsgAuditClient weMsgAuditClient;

    @Override
    public WeMsgAuditVo getPermitUserList(WeMsgAuditQuery query) {
        return weMsgAuditClient.getPermitUserList(query);
    }

    @Override
    public WeMsgAuditVo checkSingleAgree(WeMsgAuditQuery query) {
        return weMsgAuditClient.checkSingleAgree(query);
    }

    @Override
    public WeMsgAuditVo checkRoomAgree(WeMsgAuditQuery query) {
        return weMsgAuditClient.checkRoomAgree(query);
    }

    @Override
    public WeMsgAuditVo getGroupChat(WeMsgAuditQuery query) {
        return weMsgAuditClient.getGroupChat(query);
    }
}
