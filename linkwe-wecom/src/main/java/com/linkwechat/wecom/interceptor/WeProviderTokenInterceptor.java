package com.linkwechat.wecom.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.wecom.service.IQwAccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description provider_access_token请求拦截器
 * @date 2021/08/20 15:33
 **/

@Component
@Slf4j
public class WeProviderTokenInterceptor extends WeForestInterceptor implements Interceptor<WeResultVo> {

    @Value("${wecom.error-code-retry}")
    private String errorCodeRetry;

    @Override
    public boolean beforeExecute(ForestRequest request) {
        setProxy(request);
        iQwAccessTokenService = SpringUtils.getBean(IQwAccessTokenService.class);
        String token = iQwAccessTokenService.findProviderAccessToken(getCorpId(request));
        request.replaceOrAddQuery("provider_access_token", token);
        return true;
    }

    @Override
    public void onError(ForestRuntimeException e, ForestRequest forestRequest, ForestResponse forestResponse) {
        log.error("onError url:{},------params:{},----------result:{}", forestRequest.getUrl(), JSONObject.toJSONString(forestRequest.getArguments()), forestResponse.getContent());
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
     * @param weResultVo
     * @param forestRequest
     * @param forestResponse
     */
    @Override
    public void onSuccess(WeResultVo weResultVo, ForestRequest forestRequest, ForestResponse forestResponse) {
        WeErrorCodeEnum weErrorCodeEnum = WeErrorCodeEnum.parseEnum(weResultVo.getErrCode());
        if(null != weErrorCodeEnum){
            if(!weResultVo.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)){
                saveWeErrorMsg(weErrorCodeEnum,forestRequest);
            }
            weResultVo.setErrMsg(weErrorCodeEnum.getErrorMsg());
        }
        log.info("url:{},result:{}", forestRequest.getUrl(), forestResponse.getContent());
    }

    /**
     * 请求重试
     *
     * @param request  请求
     * @param response 返回值
     */
    @Override
    public void onRetry(ForestRequest request, ForestResponse response) {
        log.info("url:{}, query:{},params:{}, 重试原因:{}, 当前重试次数:{}", request.getUrl(), request.getQueryString(), JSONObject.toJSONString(request.getArguments()), response.getContent(), request.getCurrentRetryCount());
        WeResultVo weResultVo = JSONUtil.toBean(response.getContent(), WeResultVo.class);
        //当错误码符合重置token时，刷新token
        if (!ObjectUtil.equal(WeErrorCodeEnum.ERROR_CODE_OWE_1.getErrorCode(), weResultVo.getErrCode())
                && Lists.newArrayList(errorCodeRetry).contains(weResultVo.getErrCode())) {
            //删除缓存
            String corpId = getCorpId(request);
            iQwAccessTokenService.removeContactAccessToken(corpId);
            //重新查询token
            String token = iQwAccessTokenService.findProviderAccessToken(corpId);
            request.replaceOrAddQuery("provider_access_token", token);
        }
    }
}
