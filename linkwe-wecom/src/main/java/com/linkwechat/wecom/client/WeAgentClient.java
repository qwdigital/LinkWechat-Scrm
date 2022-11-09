package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.agent.query.WeAgentQuery;
import com.linkwechat.domain.wecom.vo.agent.vo.WeAgentDetailVo;
import com.linkwechat.domain.wecom.vo.agent.vo.WeAgentListVo;
import com.linkwechat.wecom.interceptor.WeAgentTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * 应用管理相关接口
 */
@BaseRequest(baseURL = "${weComServerUrl}", interceptor = WeAgentTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeAgentClient {

    /**
     * 获取企业应用列表
     * @return {@link WeAgentQuery}
     */
    @Get(url = "/agent/list")
    WeAgentListVo getList(@JSONBody WeAgentQuery query);

    /**
     * 根据应用id获取应用详情
     * @param query 应用id
     * @return {@link WeAgentDetailVo}
     */
    @Get(url = "/agent/get?agentid={$query.agentid}")
    WeAgentDetailVo getAgentDetail(@Var("query") WeAgentQuery query);

    /**
     * 设置应用
     * @param query {@link WeAgentQuery}
     * @return {@link WeResultVo}
     */
    @Post(url = "/agent/set")
    WeResultVo updateAgent(@JSONBody WeAgentQuery query);
}
