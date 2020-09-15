package com.linkwechat.wecom.service.impl;

import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeAccessTokenClient;
import com.linkwechat.wecom.domain.WeCorpAccount;
import com.linkwechat.wecom.domain.dto.WeAccessTokenDtoDto;
import com.linkwechat.wecom.service.IWeAccessTokenService;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @description: 微信token相关接口
 * @author: HaoN
 * @create: 2020-08-26 14:43
 **/
@Service
public class WeAccessTokenServiceImpl implements IWeAccessTokenService {


    @Autowired
    private WeAccessTokenClient accessTokenClient;

    @Autowired
    private IWeCorpAccountService iWxCorpAccountService;


    @Autowired
    private RedisCache redisCache;



    /**
     * 获取accessToken
     */
    @Override
    public String findCommonAccessToken() {


        return findAccessToken(WeConstans.WE_COMMON_ACCESS_TOKEN);
    }


    /**
     * 获取外部联系人相关accesstoken
     * @return
     */
    @Override
    public String findContactAccessToken() {


        return findAccessToken(WeConstans.WE_CONTACT_ACCESS_TOKEN);
    }


    private String findAccessToken(String accessTokenKey){

        String  weAccessToken =redisCache.getCacheObject(accessTokenKey);

        //为空,请求微信服务器同时缓存到redis中
        if(StringUtils.isEmpty(weAccessToken)){
            WeCorpAccount wxCorpAccount
                    = iWxCorpAccountService.findValidWeCorpAccount();
            if(null == wxCorpAccount){
                //返回错误异常，让用户绑定企业id相关信息
                throw new WeComException("无可用的corpid和secret");
            }

            WeAccessTokenDtoDto accessToken
                    = accessTokenClient.getToken(wxCorpAccount.getCorpId(),
                    WeConstans.WE_COMMON_ACCESS_TOKEN.equals(accessTokenKey)? wxCorpAccount.getCorpSecret():wxCorpAccount.getContactSecret());

            if(accessToken.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
                redisCache.setCacheObject(accessTokenKey,accessToken.getAccess_token(),accessToken.getExpires_in().intValue(), TimeUnit.SECONDS);
                weAccessToken=accessToken.getAccess_token();
            }
        }

        return weAccessToken;
    }


}
