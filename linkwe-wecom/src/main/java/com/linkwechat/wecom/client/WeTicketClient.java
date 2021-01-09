package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.WeH5TicketDto;

/**
 * @author danmo
 * @description h5获取签名
 * @date 2021/1/6 11:51
 **/
public interface WeTicketClient {
    /**
     * 获取去企业jsapi_ticket
     *
     * @return
     */
    @Request(url = "/get_jsapi_ticket")
    WeH5TicketDto getJsapiTicket();

    /**
     * 获取应用jsapi_ticket
     *
     * @return
     */
    @Request(url = "/ticket/get")
    WeH5TicketDto getTicket();
}
