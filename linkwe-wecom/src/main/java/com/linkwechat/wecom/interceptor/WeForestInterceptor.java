package com.linkwechat.wecom.interceptor;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestRequestType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.customer.WeCustomerQuery;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.wecom.service.IQwAccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${wecom.error-code-retry}")
    protected String errorCodeRetry;


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
}