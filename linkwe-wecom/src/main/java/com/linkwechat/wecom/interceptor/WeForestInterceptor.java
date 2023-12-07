package com.linkwechat.wecom.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestRequestType;
import com.dtflys.forest.http.ForestResponse;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.config.WeComeProxyConfig;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeErrorMsg;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.customer.WeCustomerQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeErrorMsgService;
import com.linkwechat.wecom.service.IQwAccessTokenService;
import com.linkwechat.wecom.utils.ForestProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 响应文件流
 */
@Slf4j
@Component
public abstract class WeForestInterceptor{

    @Autowired
    protected IQwAccessTokenService iQwAccessTokenService;

    @Autowired
    protected IWeCorpAccountService weCorpAccountService;

    @Autowired
    protected IWeErrorMsgService iWeErrorMsgService;

    @Value("${wecom.error-code-retry}")
    protected String errorCodeRetry;

    @Autowired
    protected LinkWeChatConfig linkWeChatConfig;


    protected String getCorpId(ForestRequest request) {
        String corpId;
        Map<String, Object> baseQuery = request.getBody().nameValuesMapWithObject();
        if (baseQuery == null && baseQuery.get("corpid") == null) {
            WeBaseQuery query = (WeBaseQuery) request.getVariableValue("query");
            corpId = query.getCorpid();
            if (request.getVariableValue("query") instanceof WeCustomerQuery) {
                WeCustomerQuery weCustomerQuery = (WeCustomerQuery) request.getVariableValue("query");
                if (request.getType().getName().equals(ForestRequestType.GET.getName()) && StringUtils.isNotEmpty(weCustomerQuery.getCursor())) {
                    request.addQuery("cursor", weCustomerQuery.getCursor());
                }
            }
        } else {
            corpId = baseQuery.get("corpid") == null ? null : String.valueOf(baseQuery.get("corpid"));
        }
        return corpId;
    }


    @Async
    protected void saveWeErrorMsg(WeErrorCodeEnum weErrorCodeEnum, ForestRequest forestRequest){
        iWeErrorMsgService.save(
                WeErrorMsg.builder()
                        .errorMsg(weErrorCodeEnum.getErrorMsg())
                        .errorCode(weErrorCodeEnum.getErrorCode().longValue())
                        .url(forestRequest.getUrl())
                        .weParams(JSONObject.toJSONString(forestRequest.getArguments()))
                        .build()
        );
    }


    protected void setProxy(ForestRequest request){
        WeComeProxyConfig weComeProxyConfig
                = linkWeChatConfig.getWeComeProxyConfig();
        if(null != weComeProxyConfig){
            if(weComeProxyConfig.isStartProxy()&&StringUtils.isNotEmpty(weComeProxyConfig.getProxyIp())){

                ForestProxyUtils.setProxy(request,weComeProxyConfig.getProxyIp(),weComeProxyConfig.getProxyPort(),
                        weComeProxyConfig.getProxyPassword(), weComeProxyConfig.getProxyUserName());

            }
        }


    }
}