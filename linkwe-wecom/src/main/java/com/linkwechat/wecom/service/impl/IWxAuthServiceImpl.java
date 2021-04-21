package com.linkwechat.wecom.service.impl;

import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.weixin.dto.WxAuthUserInfoDto;
import com.linkwechat.wecom.domain.weixin.dto.WxTokenDto;
import com.linkwechat.wecom.service.IWxAuthService;
import com.linkwechat.wecom.wxclient.WxAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author danmo
 * @description
 * @date 2021/4/5 22:45
 **/
@Slf4j
@Service
public class IWxAuthServiceImpl implements IWxAuthService {
    @Autowired
    private WxAuthClient wxAuthClient;
    @Autowired
    private RedisCache redisCache;

    @Value("${weixin.appid}")
    private String appId;
    @Value("${weixin.secret}")
    private String secret;
    private final String grantType = "authorization_code";

    @Override
    public WxTokenDto getToken(String code,String openId) {
        WxTokenDto authToken;
        if (StringUtils.isNotEmpty(openId)){
            authToken =redisCache.getCacheObject(WeConstans.WX_AUTH_ACCESS_TOKEN+":"+ openId);
        }else {
            authToken = wxAuthClient.getAuthToken(appId, secret, code, grantType);
            if(authToken != null && StringUtils.isNotEmpty(authToken.getAccessToken())){
                redisCache.setCacheObject(WeConstans.WX_AUTH_ACCESS_TOKEN+":"+ authToken.getOpenId(),
                        authToken, authToken.getExpiresIn(), TimeUnit.SECONDS);
                redisCache.setCacheObject(WeConstans.WX_AUTH_REFRESH_ACCESS_TOKEN+":"+ authToken.getOpenId(),
                        authToken.getRefreshToken(), 30, TimeUnit.DAYS);
            }
        }
        return authToken;
    }

    @Override
    public WxAuthUserInfoDto getUserInfo(String openId, String lang) {
        return wxAuthClient.getUserInfo(openId, lang);
    }
}
