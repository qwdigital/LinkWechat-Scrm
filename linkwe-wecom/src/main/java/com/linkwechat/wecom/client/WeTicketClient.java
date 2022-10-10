package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Retry;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.vo.third.auth.WeTicketVo;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @author danmo
 * @description h5获取签名
 * @date 2021/1/6 11:51
 **/
@BaseRequest(baseURL = "${weComServerUrl}", interceptor = WeAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeTicketClient {
    /**
     * 获取去企业jsapi_ticket
     *
     * @return
     */
    @Get(url = "/get_jsapi_ticket")
    WeTicketVo getCorpTicket(@JSONBody WeBaseQuery query);

    /**
     * 获取应用jsapi_ticket
     *
     * @return
     */
    @Get(url = "/ticket/get?type=agent_config")
    WeTicketVo getAppTicket(@JSONBody WeBaseQuery query);
}
