package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.wecom.domain.dto.WeAccessTokenDto;
import com.linkwechat.wecom.interceptor.WeNoTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @description: 获取企业微信Token相关
 * @author: HaoN
 * @create: 2020-08-26 14:33
 **/
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeNoTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeAccessTokenClient {

    /**
     *  获取token(常用token,联系人token)
     * @param corpId 企业id
     * @param corpSecret 应用的凭证密钥
     * @return {@link WeAccessTokenDto}
     */
    @Request(url = "/gettoken")
    WeAccessTokenDto getToken(@Query("corpid") String corpId, @Query("corpsecret") String corpSecret);

}