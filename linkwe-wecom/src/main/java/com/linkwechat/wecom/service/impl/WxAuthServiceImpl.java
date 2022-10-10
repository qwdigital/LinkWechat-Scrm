package com.linkwechat.wecom.service.impl;

import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.wecom.vo.weixin.WxAuthUserInfoVo;
import com.linkwechat.domain.wecom.vo.weixin.WxTokenVo;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.wecom.service.IWxAuthService;
import com.linkwechat.wecom.wxclient.WxAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author danmo
 * @description
 * @date 2021/4/5 22:45
 **/
@Slf4j
@Service
public class WxAuthServiceImpl implements IWxAuthService {
    @Autowired
    private WxAuthClient wxAuthClient;
    @Autowired
    private RedisService redisService;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;


    private final String grantType = "authorization_code";


    @Override
    public WxTokenVo getToken(String code) {
        WeCorpAccount corpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
        WxTokenVo authToken = wxAuthClient.getAuthToken(corpAccount.getWxAppId(), corpAccount.getWxSecret(), code, grantType);
        if(authToken != null && StringUtils.isNotEmpty(authToken.getAccessToken())){
            redisService.setCacheObject(WeConstans.WX_AUTH_ACCESS_TOKEN+":"+ authToken.getOpenId(),
                    authToken, authToken.getExpiresIn().intValue(), TimeUnit.SECONDS);
            redisService.setCacheObject(WeConstans.WX_AUTH_REFRESH_ACCESS_TOKEN+":"+ authToken.getOpenId(),
                    authToken.getRefreshToken(), 30, TimeUnit.DAYS);
        }
        return authToken;
    }

    @Override
    public WxAuthUserInfoVo getUserInfo(String openId, String lang) {
        return wxAuthClient.getUserInfo(openId, lang);
    }

}
