package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.annotation.Retry;
import com.linkwechat.wecom.domain.WeH5TicketDto;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeAppAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @author danmo
 * @description h5获取签名
 * @date 2021/1/6 11:51
 **/
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeAppAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeTicketClient {
    /**
     * 获取去企业jsapi_ticket
     *
     * @return
     */
    @Request(url = "/get_jsapi_ticket")
    WeH5TicketDto getJsapiTicket(@Header("agentId") String agentId);

    /**
     * 获取应用jsapi_ticket
     *
     * @return
     */
    @Request(url = "/ticket/get?type=agent_config")
    WeH5TicketDto getTicket(@Header("agentId") String agentId);
}
