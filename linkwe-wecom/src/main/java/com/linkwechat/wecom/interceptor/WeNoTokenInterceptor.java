package com.linkwechat.wecom.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.config.WeComeProxyConfig;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.wecom.service.IQwAccessTokenService;
import com.linkwechat.wecom.utils.ForestProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 无需token请求拦截器
 * @date 2021/08/20 15:33
 **/

@Component
@Slf4j
public class WeNoTokenInterceptor implements Interceptor {


    @Autowired
    protected LinkWeChatConfig linkWeChatConfig;

    @Override
    public boolean beforeExecute(ForestRequest request) {
        WeComeProxyConfig weComeProxyConfig
                = linkWeChatConfig.getWeComeProxyConfig();
        if(null != weComeProxyConfig){
            if(weComeProxyConfig.isStartProxy()&&StringUtils.isNotEmpty(weComeProxyConfig.getProxyIp())){

                ForestProxyUtils.setProxy(request,weComeProxyConfig.getProxyIp(),weComeProxyConfig.getProxyPort(),
                        weComeProxyConfig.getProxyPassword(), weComeProxyConfig.getProxyUserName());

            }
        }

        return true;
    }

    @Override
    public void onError(ForestRuntimeException e, ForestRequest forestRequest, ForestResponse forestResponse) {
        log.error("onError url:{},------params:{},----------result:{}",forestRequest.getUrl(), JSONObject.toJSONString(forestRequest.getArguments()), forestResponse.getContent());
        if (StringUtils.isNotEmpty(forestResponse.getContent())) {
            WeComException weComException = new WeComException(1001, forestResponse.getContent());
            throw new ForestRuntimeException(weComException);
        } else {
            WeComException weComException = new WeComException(-1, "网络请求超时");
            throw new ForestRuntimeException(weComException);
        }
    }

    /**
     * 请求成功调用(微信端错误异常统一处理)
     *
     * @param resultDto
     * @param forestRequest
     * @param forestResponse
     */
    @Override
    public void onSuccess(Object resultDto, ForestRequest forestRequest, ForestResponse forestResponse) {
        log.info("url:{},result:{}", forestRequest.getUrl(), forestResponse.getContent());
    }


    @Override
    public void onRetry(ForestRequest request, ForestResponse response) {
        log.info("url:{}, params:{}, 重试原因:{}, 当前重试次数:{}",request.getUrl(), JSONObject.toJSONString(request.getArguments()),response.getContent(), request.getCurrentRetryCount());
    }
}
