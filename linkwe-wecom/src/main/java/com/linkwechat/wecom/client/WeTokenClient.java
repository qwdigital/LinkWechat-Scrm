package com.linkwechat.wecom.client;

import com.linkwechat.domain.wecom.query.WeCorpTokenQuery;
import com.linkwechat.domain.wecom.query.WeProvideTokenQuery;
import com.linkwechat.domain.wecom.query.WeSuiteTokenQuery;
import com.linkwechat.domain.wecom.vo.WeCorpTokenVo;
import com.linkwechat.wecom.interceptor.WeNoTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;
import com.dtflys.forest.annotation.*;

/**
 * @author danmo
 * @description 获取token接口
 * @date 2021/12/13 10:30
 **/
@BaseRequest(baseURL = "${weComServerUrl}")
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeTokenClient {
    /**
     * 获取企业凭证
     *
     * @param corpId 企业ID
     * @param scret  企业ID
     * @return WeCorpTokenVo
     */
    @Request(url = "/gettoken", type = "GET", interceptor = WeNoTokenInterceptor.class)
    WeCorpTokenVo getToken(@Query("corpid") String corpId, @Query("corpsecret") String scret);

    /**
     * 获取服务商凭证
     *
     * @param query
     * @return WeCorpTokenVo
     */
    @Request(url = "/service/get_provider_token", type = "POST", interceptor = WeNoTokenInterceptor.class)
    WeCorpTokenVo getProviderToken(@JSONBody WeProvideTokenQuery query);

}
