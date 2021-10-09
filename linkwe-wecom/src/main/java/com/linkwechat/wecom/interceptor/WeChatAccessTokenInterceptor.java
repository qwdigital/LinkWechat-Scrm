package com.linkwechat.wecom.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.linkwechat.common.config.WeComeConfig;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.service.IWeAccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 会话token拦截器
 * @author: danmo
 * @create: 2021-09-27 22:36
 **/
@Slf4j
@Component
public class WeChatAccessTokenInterceptor implements Interceptor<WeResultDto> {


    @Autowired
    private IWeAccessTokenService iWeAccessTokenService;

    @Autowired
    private WeComeConfig weComeConfig;


    /**
     * 该方法在请求发送之前被调用, 若返回false则不会继续发送请求
     */
    @Override
    public boolean beforeExecute(ForestRequest request) {
        String token = iWeAccessTokenService.findChatAccessToken();
        request.replaceOrAddQuery("access_token", token);
        return true;
    }


    /**
     * 请求发送失败时被调用
     *
     * @param e
     * @param forestRequest
     * @param forestResponse
     */
    @Override
    public void onError(ForestRuntimeException e, ForestRequest forestRequest, ForestResponse forestResponse) {
        log.info("url:{},------params:{},----------result:" + forestRequest.getUrl(), JSONObject.toJSONString(forestRequest.getArguments()), forestResponse.getContent());
        if (StringUtils.isNotEmpty(forestResponse.getContent())) {
            throw new ForestRuntimeException(forestResponse.getContent());
        } else {
            throw new ForestRuntimeException("网络请求超时");
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
    public void onSuccess(WeResultDto resultDto, ForestRequest forestRequest, ForestResponse forestResponse) {
        log.info("url:{},result:{}", forestRequest.getUrl(), forestResponse.getContent());
        if (null != resultDto.getErrcode() && !ObjectUtil.equal(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode(), resultDto.getErrcode())
                && !weComeConfig.getWeNeedRetryErrorCodes().contains(resultDto.getErrcode())) {
            throw new ForestRuntimeException(WeErrorCodeEnum.parseEnum(resultDto.getErrcode()).getErrorMsg());
        }
    }

    /**
     * 请求重试
     * @param request 请求
     * @param response 返回值
     */
    @Override
    public void onRetry(ForestRequest request, ForestResponse response) {
        log.info("url:{}, params:{}, 重试原因:{}, 当前重试次数:{}",request.getUrl(), JSONObject.toJSONString(request.getArguments()),response.getContent(), request.getCurrentRetryCount());
        WeResultDto weResultDto = JSONUtil.toBean(response.getContent(), WeResultDto.class);
        //当错误码符合重置token时，刷新token
        if(!ObjectUtil.equal(WeErrorCodeEnum.ERROR_CODE_OWE_1.getErrorCode(),weResultDto.getErrcode())
                && weComeConfig.getWeNeedRetryErrorCodes().contains(weResultDto.getErrmsg())){
            iWeAccessTokenService.findChatAccessToken();
            String token = iWeAccessTokenService.findContactAccessToken();
            request.replaceOrAddQuery("access_token", token);
        }
    }
}
