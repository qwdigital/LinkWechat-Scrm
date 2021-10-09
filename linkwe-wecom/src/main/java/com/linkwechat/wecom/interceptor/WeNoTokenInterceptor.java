package com.linkwechat.wecom.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.linkwechat.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 无需token请求拦截器
 * @date 2021/08/20 15:33
 **/

@Component
@Slf4j
public class WeNoTokenInterceptor implements Interceptor {

    @Override
    public void onError(ForestRuntimeException e, ForestRequest forestRequest, ForestResponse forestResponse) {
        log.info("url:{},------params:{},----------result:"+forestRequest.getUrl(), JSONObject.toJSONString(forestRequest.getArguments()), forestResponse.getContent());
        if (StringUtils.isNotEmpty(forestResponse.getContent())){
            throw new ForestRuntimeException(forestResponse.getContent());
        }else {
            throw new ForestRuntimeException("网络请求超时");
        }
    }

    @Override
    public void onRetry(ForestRequest request, ForestResponse response) {
        log.info("url:{}, params:{}, 重试原因:{}, 当前重试次数:{}",request.getUrl(), JSONObject.toJSONString(request.getArguments()),response.getContent(), request.getCurrentRetryCount());
    }
}
