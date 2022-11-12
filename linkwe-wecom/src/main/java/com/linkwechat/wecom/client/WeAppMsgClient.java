package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.domain.wecom.query.msg.WeAppMsgQuery;
import com.linkwechat.domain.wecom.query.msg.WeRecallMsgQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.msg.WeAppMsgVo;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeAgentTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @author danmo
 * @Description 客户接口
 * @date 2021/12/2 16:46
 **/

@BaseRequest(baseURL = "${weComServerUrl}")
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeAppMsgClient {

    /**
     * 发送应用消息
     *
     * @param query
     * @return WeAppMsgVo
     */
    @Post(url = "/message/send",interceptor = WeAccessTokenInterceptor.class)
    WeAppMsgVo sendAppMsg(@JSONBody WeAppMsgQuery query);

    @Post(url = "/message/recall",interceptor = WeAgentTokenInterceptor.class)
    WeResultVo recallMsg(@JSONBody WeRecallMsgQuery query);

    @Post(url = "/message/recall",interceptor = WeAgentTokenInterceptor.class)
    WeResultVo recallAgentMsg(@JSONBody WeRecallMsgQuery query);
}
