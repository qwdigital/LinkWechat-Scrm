package com.linkwechat.wecom.wxclient;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.domain.wecom.vo.third.auth.WeTicketVo;
import com.linkwechat.domain.wecom.vo.weixin.WxTokenVo;
import com.linkwechat.wecom.interceptor.WeiXinAccessTokenInterceptor;

/**
 * @author danmo
 * @description
 * @date 2021/4/5 17:11
 **/
@BaseRequest(baseURL = "${wxCommonServerUrl}",contentType = "application/json")
public interface WxCommonClient {
    /**
     * 获取微信token
     *
     * @param grantType 填写为client_credential
     * @param appId     公众号的唯一标识
     * @param secret    密钥
     * @return
     */
    @Request(url = "/token", type = "GET")
    WxTokenVo getToken(@Query("grant_type") String grantType, @Query("appid") String appId, @Query("secret") String secret);

    /**
     * 获取ticket票据
     * @param type
     * @return
     */
    @Request(url = "/ticket/getticket", type = "GET",interceptor = WeiXinAccessTokenInterceptor.class)
    WeTicketVo getTicket(@Query("type") String type);


}
