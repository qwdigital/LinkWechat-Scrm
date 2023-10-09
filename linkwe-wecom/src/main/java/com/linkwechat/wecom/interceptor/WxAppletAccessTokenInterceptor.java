package com.linkwechat.wecom.interceptor;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.wecom.query.WxAppletBaseQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.weixin.WxTokenVo;
import com.linkwechat.wecom.wxclient.WxCommonClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author danmo
 * @description 微信小程序请求拦截器
 * @date 2021/4/5 15:17
 **/
@Slf4j
@Component
public class WxAppletAccessTokenInterceptor extends WeForestInterceptor implements Interceptor {

    private final String grantType = "client_credential";

    private WxCommonClient wxCommonClient;

    private RedisService redisService;

    @Override
    public boolean beforeExecute(ForestRequest request) {
        setProxy(request);
        if (wxCommonClient == null) {
            wxCommonClient = SpringUtils.getBean(WxCommonClient.class);
        }

        if (redisService == null) {
            redisService = SpringUtils.getBean(RedisService.class);
        }
        String accessToken;
        Map<String, Object> baseQuery = request.getBody().nameValuesMapWithObject();
        if (baseQuery == null && baseQuery.get("accessToken") == null) {
            WxAppletBaseQuery query = (WxAppletBaseQuery) request.getVariableValue("query");
            accessToken = query.getAccessToken();
        } else {
            accessToken = baseQuery.get("accessToken") == null ? null : String.valueOf(baseQuery.get("accessToken"));
        }

        if (StringUtils.isNotEmpty(accessToken)) {
            request.addQuery("access_token", accessToken);
        } else {
            accessToken = findAccessToken();
            request.addQuery("access_token", accessToken);
        }
        return true;
    }


    private String findAccessToken() {
        //获取用户token
        String accessToken = redisService.getCacheObject(WeConstans.WX_APPLET_ACCESS_TOKEN);
        if (StringUtils.isEmpty(accessToken)) {
            WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
            if (Objects.nonNull(weCorpAccount)) {
                //当用户token失效，重新获取token
                WxTokenVo wxTokenVo = wxCommonClient.getToken(grantType, weCorpAccount.getMiniAppId(), weCorpAccount.getMiniSecret());
                if (wxTokenVo != null && StringUtils.isNotEmpty(wxTokenVo.getAccessToken())) {
                    redisService.setCacheObject(WeConstans.WX_APPLET_ACCESS_TOKEN, wxTokenVo.getAccessToken(), wxTokenVo.getExpiresIn().intValue(), TimeUnit.SECONDS);
                    accessToken = wxTokenVo.getAccessToken();
                }
            }
        }
        return accessToken;
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
        log.error("onError url:{},------params:{},----------result:{}", forestRequest.getUrl(), JSONObject.toJSONString(forestRequest.getArguments()), forestResponse.getContent());
        if (StringUtils.isNotEmpty(forestResponse.getContent())) {
            WeComException weComException = new WeComException(1001, forestResponse.getContent());
            throw new ForestRuntimeException(weComException);
        } else {
            WeComException weComException = new WeComException(-1, "网络请求超时");
            throw new ForestRuntimeException(weComException);
        }
    }

    @Override
    public void onSuccess(Object data, ForestRequest request, ForestResponse response) {
        log.info("url:【{}】,result:【{}】", request.getUrl(), response.getContent());
        WeResultVo weResultVo = JSONUtil.toBean(response.getContent(), WeResultVo.class);
        if (null != weResultVo.getErrCode() && !weResultVo.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)) {
            throw new ForestRuntimeException(response.getContent());
        }
    }
}
