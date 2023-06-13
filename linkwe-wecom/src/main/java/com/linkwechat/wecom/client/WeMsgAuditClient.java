package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.domain.wecom.query.msgaudit.WeMsgAuditQuery;
import com.linkwechat.domain.wecom.vo.msgaudit.WeMsgAuditVo;
import com.linkwechat.wecom.interceptor.WeChatAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @author danmo
 * @description 会话存档接口
 * @date 2020/12/2 16:45
 **/
@BaseRequest(baseURL = "${weComServerUrl}", interceptor = WeChatAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeMsgAuditClient {

    /**
     * 获取会话内容存档开启成员列表
     */
    @Post(url = "/msgaudit/get_permit_user_list")
    WeMsgAuditVo getPermitUserList(@JSONBody WeMsgAuditQuery query);

    /**
     * 单聊 获取会话中外部成员的同意情况
     * @param query
     * @return
     */
    @Post(url = "/msgaudit/check_single_agree")
    WeMsgAuditVo checkSingleAgree(@JSONBody WeMsgAuditQuery query);

    /**
     * 群聊 获取群会话中外部成员的同意情况
     * @param query
     * @return
     */
    @Post(url = "/msgaudit/check_room_agree")
    WeMsgAuditVo checkRoomAgree(@JSONBody WeMsgAuditQuery query);

    /**
     * 获取会话内容存档内部群信息
     * @param query
     * @return
     */
    @Post(url = "/msgaudit/groupchat/get")
    WeMsgAuditVo getGroupChat(@JSONBody WeMsgAuditQuery query);
}
