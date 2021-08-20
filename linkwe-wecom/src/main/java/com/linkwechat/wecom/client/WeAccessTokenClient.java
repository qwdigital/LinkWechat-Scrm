package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.WeAccessTokenDtoDto;
import com.linkwechat.wecom.domain.dto.WeLoginUserInfoDto;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;

/**
 * @description: 获取企业微信Token相关
 * @author: HaoN
 * @create: 2020-08-26 14:33
 **/
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeAccessTokenInterceptor.class)
public interface WeAccessTokenClient {
    /**
     *  获取token(常用token,联系人token)
     * @param corpId 企业id
     * @param corpSecret 应用的凭证密钥
     * @return
     */
    @Request(url = "/gettoken")
    WeAccessTokenDtoDto getToken(@Query("corpid") String corpId, @Query("corpsecret") String corpSecret);
}