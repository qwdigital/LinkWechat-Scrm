package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.wecom.domain.dto.WeAppDetailDto;
import com.linkwechat.wecom.domain.dto.WeAppDto;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeAppAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * 应用管理相关接口
 */
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeAppAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeAppClient {

    /**
     * 获取企业应用列表
     * @return {@link WeAppDto}
     */
    @Request(url = "/agent/list")
    WeAppDto  findAgentList();

    /**
     * 根据应用id获取应用详情
     * @param agentid 应用id
     * @return {@link WeAppDetailDto}
     */
    @Request(url = "/agent/get")
    WeAppDetailDto  findAgentById(@Query("agentid") String agentid);

    /**
     * 设置应用
     * @param weAppDetailDto {@link WeAppDetailDto}
     * @return {@link WeResultDto}
     */
    @Request(url = "/agent/set",type = "POST")
    WeResultDto updateAgentById(@JSONBody WeAppDetailDto weAppDetailDto,@Header("agentid")String agentid);
}
