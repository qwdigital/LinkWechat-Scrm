package com.linkwechat.wecom.interceptor;

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
import com.linkwechat.domain.wecom.vo.weixin.WxTokenVo;
import com.linkwechat.wecom.wxclient.WxAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author danmo
 * @description 微信授权拦截器
 * @date 2021/4/5 15:28
 **/

@Slf4j
@Component
public class WeiXinAuthInterceptor extends WeForestInterceptor implements Interceptor {

    private final String grantType = "refresh_token";

    @Resource
    private WxAuthClient wxAuthClient;

    private final RedisService redisService = SpringUtils.getBean(RedisService.class);

    @Override
    public boolean beforeExecute(ForestRequest request) {
        setProxy(request);
        Object openId = request.getQuery("openid");
        if (openId != null) {
            String accessToken = findAccessToken(String.valueOf(openId));
            request.replaceOrAddQuery("access_token", accessToken);
        }
        return true;
    }


    private String findAccessToken(String openId) {
        //获取用户token
        WxTokenVo wxTokenVo = redisService.getCacheObject(WeConstans.WX_AUTH_ACCESS_TOKEN + ":" + openId);
        if (wxTokenVo == null) {
            //当用户token失效，则获取refreshToken
            String refreshToken = redisService.getCacheObject(WeConstans.WX_AUTH_REFRESH_ACCESS_TOKEN + ":" + openId);
            if (StringUtils.isEmpty(refreshToken)) {
                throw new WeComException(1001, "token失效，请重新授权");
            } else {
                WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
                if (Objects.nonNull(weCorpAccount)) {
                    wxTokenVo = wxAuthClient.refreshToken(weCorpAccount.getWxAppId(), grantType, refreshToken);
                    if (wxTokenVo != null && StringUtils.isNotEmpty(wxTokenVo.getAccessToken())) {
                        redisService.setCacheObject(WeConstans.WX_AUTH_ACCESS_TOKEN + ":" + openId, wxTokenVo, wxTokenVo.getExpiresIn().intValue(), TimeUnit.SECONDS);
                    }
                }

            }
        }
        return wxTokenVo.getAccessToken();
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
    }
}
