package com.linkwechat.wecom.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestProxy;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.google.common.collect.Lists;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.config.WeComeProxyConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.wecom.service.IQwAccessTokenService;
import com.linkwechat.wecom.utils.ForestProxyUtils;
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
public class WeAccessTokenFileInterceptor extends WeForestInterceptor implements Interceptor<Object> {




    /**
     * 该方法在请求发送之前被调用, 若返回false则不会继续发送请求
     */
    @Override
    public boolean beforeExecute(ForestRequest request) {

         setProxy(request);



        if (iQwAccessTokenService == null) {
            iQwAccessTokenService = SpringUtils.getBean(IQwAccessTokenService.class);
        }
        String token = iQwAccessTokenService.findCommonAccessToken(getCorpId(request));
        request.replaceOrAddQuery("access_token", token);
        return true;
    }

    /**
     * 请求成功调用(微信端错误异常统一处理)
     */
    @Override
    public void onSuccess(Object resultDto, ForestRequest request, ForestResponse response) {
//        WeErrorCodeEnum weErrorCodeEnum = WeErrorCodeEnum.parseEnum(resultDto.getErrCode());
//        if(null != weErrorCodeEnum){
//            if(!resultDto.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)){
//                saveWeErrorMsg(weErrorCodeEnum,request);
//            }
//            resultDto.setErrMsg(weErrorCodeEnum.getErrorMsg());
//        }
        log.info("url:{},result:{}", request.getUrl(), response.getContent());
    }

    @Override
    public void onError(ForestRuntimeException ex, ForestRequest forestRequest, ForestResponse forestResponse) {
        log.error("onError url:{},------params:{},----------result:{}", forestRequest.getUrl(),
                JSONObject.toJSONString(forestRequest.getArguments()), forestResponse.getContent());
        if (StringUtils.isNotEmpty(forestResponse.getContent())) {
            WeComException weComException = new WeComException(1001, forestResponse.getContent());
            throw new ForestRuntimeException(weComException);
        } else {
            WeComException weComException = new WeComException(-1, "网络请求超时");
            throw new ForestRuntimeException(weComException);
        }
    }

    /**
     * 请求重试
     *
     * @param request  请求
     * @param response 返回值
     */
    @Override
    public void onRetry(ForestRequest request, ForestResponse response) {
        log.info("url:{}, query:{},params:{}, 重试原因:{}, 当前重试次数:{}", request.getUrl(), request.getQueryString(),
                JSONObject.toJSONString(request.getArguments()), response.getContent(), request.getCurrentRetryCount());
        WeResultVo weResultVo = JSONUtil.toBean(response.getContent(), WeResultVo.class);
        //当错误码符合重置token时，刷新token
        if (!ObjectUtil.equal(WeErrorCodeEnum.ERROR_CODE_OWE_1.getErrorCode(), weResultVo.getErrCode())
                && Lists.newArrayList(errorCodeRetry).contains(weResultVo.getErrCode())) {
            //删除缓存
            String corpId = getCorpId(request);
            iQwAccessTokenService.removeCommonAccessToken(corpId);
            String token = iQwAccessTokenService.findCommonAccessToken(corpId);
            request.replaceOrAddQuery("access_token", token);
            request.execute();
        }
    }

}