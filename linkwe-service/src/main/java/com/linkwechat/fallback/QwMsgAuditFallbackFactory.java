package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.msgaudit.WeMsgAuditQuery;
import com.linkwechat.domain.wecom.vo.msgaudit.WeMsgAuditVo;
import com.linkwechat.fegin.QwMsgAuditClient;

/**
 * @author danmo
 * @date 2023年05月12日 10:17
 */
public class QwMsgAuditFallbackFactory implements QwMsgAuditClient {
    @Override
    public AjaxResult<WeMsgAuditVo> getPermitUserList(WeMsgAuditQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeMsgAuditVo> checkSingleAgree(WeMsgAuditQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeMsgAuditVo> checkRoomAgree(WeMsgAuditQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeMsgAuditVo> getGroupChat(WeMsgAuditQuery query) {
        return null;
    }
}
