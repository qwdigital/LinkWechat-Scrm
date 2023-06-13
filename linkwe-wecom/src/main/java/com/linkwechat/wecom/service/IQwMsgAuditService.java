package com.linkwechat.wecom.service;

import com.linkwechat.domain.wecom.query.msgaudit.WeMsgAuditQuery;
import com.linkwechat.domain.wecom.vo.msgaudit.WeMsgAuditVo;

/**
 * 会话存档业务接口
 *
 * @author danmo
 * @date 2023年05月12日 10:01
 */
public interface IQwMsgAuditService {
    /**
     * 获取会话内容存档开启成员列表
     * @param query
     * @return
     */
    WeMsgAuditVo getPermitUserList(WeMsgAuditQuery query);

    WeMsgAuditVo checkSingleAgree(WeMsgAuditQuery query);

    WeMsgAuditVo checkRoomAgree(WeMsgAuditQuery query);

    WeMsgAuditVo getGroupChat(WeMsgAuditQuery query);
}
