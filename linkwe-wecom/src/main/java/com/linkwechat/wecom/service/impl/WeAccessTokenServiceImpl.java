package com.linkwechat.wecom.service.impl;

import com.linkwechat.wecom.client.WeAccessTokenClient;
import com.linkwechat.wecom.domain.dto.WeAccessTokenDtoDto;
import com.linkwechat.wecom.domain.WeCorpAccount;
import com.linkwechat.wecom.service.IWeAccessTokenService;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @description: 微信token相关接口
 * @author: My
 * @create: 2020-08-26 14:43
 **/
@Service
public class WeAccessTokenServiceImpl implements IWeAccessTokenService {


    @Autowired
    private WeAccessTokenClient accessTokenClient;

    @Autowired
    private IWeCorpAccountService iWxCorpAccountService;


    @Autowired
    private   RedisCache redisCache;



    /**
     * 获取accessToken
     */
    @Override
    public String findToken() {
        WeCorpAccount wxCorpAccount
                = iWxCorpAccountService.findValidWeCorpAccount();
        if(null == wxCorpAccount){
             //返回错误异常，让用户绑定企业id相关信息
        }

        String  weAccessToken = (String) redisCache.getCacheObject(WeConstans.WE_ACCESS_TOKEN);

        //为空,请求微信服务器同时缓存到redis中
        if(StringUtils.isEmpty(weAccessToken)){
            WeAccessTokenDtoDto accessToken
                = accessTokenClient.getToken(wxCorpAccount.getCorpId(), wxCorpAccount.getCorpSecret());
            if(accessToken.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
                redisCache.setCacheObject(WeConstans.WE_ACCESS_TOKEN,accessToken.getAccess_token(),accessToken.getExpires_in().intValue(), TimeUnit.SECONDS);
                weAccessToken=accessToken.getAccess_token();
            }

        }


        return  weAccessToken;
    }





}
