package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.domain.wecom.query.msg.WeAppMsgQuery;
import com.linkwechat.domain.wecom.vo.msg.WeAppMsgVo;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @author danmo
 * @Description 客户接口
 * @date 2021/12/2 16:46
 **/

@BaseRequest(baseURL = "${weComServerUrl}", interceptor = WeAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeAppMsgClient {

    /**
     * 发送应用消息
     *
     * @param query
     * @return WeAppMsgVo
     */
    @Post(url = "/message/send")
    WeAppMsgVo sendAppMsg(@JSONBody WeAppMsgQuery query);
}
