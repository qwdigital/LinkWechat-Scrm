package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.WeH5TicketDto;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;

/**
 * @author danmo
 * @description h5获取签名
 * @date 2021/1/6 11:51
 **/
//@BaseRequest(interceptor = WeAccessTokenInterceptor.class)
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
    @Request(url = "/ticket/get")
    WeH5TicketDto getTicket(@Header("agentId") String agentId);
}
