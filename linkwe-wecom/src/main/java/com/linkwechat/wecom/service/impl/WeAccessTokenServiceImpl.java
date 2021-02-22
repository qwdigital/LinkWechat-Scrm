package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeAccessTokenClient;
import com.linkwechat.wecom.domain.WeApp;
import com.linkwechat.wecom.domain.WeCorpAccount;
import com.linkwechat.wecom.domain.dto.WeAccessTokenDtoDto;
import com.linkwechat.wecom.service.IWeAccessTokenService;
import com.linkwechat.wecom.service.IWeAppService;
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


    @Autowired
    private IWeAppService iWeAppService;



    /**
     * 获取通用accessToken
     */
    @Override
    public String findCommonAccessToken() {


        return findAccessToken(WeConstans.WE_COMMON_ACCESS_TOKEN);
    }


    /**
     * 获取外部联系人相关token
     * @return
     */
    @Override
    public String findContactAccessToken() {


        return findAccessToken(WeConstans.WE_CONTACT_ACCESS_TOKEN);
    }


    /**
     * 获取服务商相关token
     * @return
     */
    @Override
    public String findProviderAccessToken() {
        return findAccessToken(WeConstans.WE_PROVIDER_ACCESS_TOKEN);
    }


    /**
     * 会话存档相关token
     * @return
     */
    @Override
    public String findChatAccessToken() {
        return findAccessToken(WeConstans.WE_CHAT_ACCESS_TOKEN);
    }



    /**
     * 获取第三方应用所需要的token
     * @param agentId
     * @return
     */
    @Override
    public String findThirdAppAccessToken(String agentId) {


        String token=redisCache.getCacheObject(WeConstans.WE_THIRD_APP_TOKEN+"::"+agentId);

        if(StringUtils.isNotEmpty(token)){
            return token;
        }

        WeApp weApp = iWeAppService.getOne(new LambdaQueryWrapper<WeApp>()
                .eq(WeApp::getAgentId, agentId)
                .eq(WeApp::getDelFlag, Constants.NORMAL_CODE)
                .eq(WeApp::getStatus,  Constants.NORMAL_CODE));
        if(weApp == null){
            throw new WeComException("当前agentId不可用或不存在");
        }

        WeCorpAccount wxCorpAccount
                = iWxCorpAccountService.findValidWeCorpAccount();
        if(wxCorpAccount == null){
            throw new WeComException("无可用的corpid和secret");
        }

        WeAccessTokenDtoDto weAccessTokenDtoDto
                = accessTokenClient.getToken(wxCorpAccount.getCorpId(),weApp.getAgentSecret());
        token=weAccessTokenDtoDto.getAccess_token();
        if(StringUtils.isNotEmpty(token)){

            redisCache.setCacheObject(WeConstans.WE_THIRD_APP_TOKEN+"::"+agentId,token,weAccessTokenDtoDto.getExpires_in().intValue(), TimeUnit.SECONDS);
        }


        return token;
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
            String token="";
            Long expires_in=null;
            if(WeConstans.WE_COMMON_ACCESS_TOKEN.equals(accessTokenKey) || WeConstans.WE_CONTACT_ACCESS_TOKEN.equals(accessTokenKey)){
                WeAccessTokenDtoDto weAccessTokenDtoDto = accessTokenClient.getToken(wxCorpAccount.getCorpId(),
                        WeConstans.WE_COMMON_ACCESS_TOKEN.equals(accessTokenKey) ? wxCorpAccount.getCorpSecret() : wxCorpAccount.getContactSecret());
                token=weAccessTokenDtoDto.getAccess_token();
                expires_in=weAccessTokenDtoDto.getExpires_in();

            }else  if(WeConstans.WE_PROVIDER_ACCESS_TOKEN.equals(accessTokenKey)){
                WeAccessTokenDtoDto providerToken = accessTokenClient.getProviderToken(wxCorpAccount.getCorpId(), wxCorpAccount.getProviderSecret());
                token=providerToken.getProvider_access_token();
                expires_in=providerToken.getExpires_in();
            }else if (WeConstans.WE_CHAT_ACCESS_TOKEN.equals(accessTokenKey)){
                WeAccessTokenDtoDto weAccessTokenDtoDto = accessTokenClient.getToken(wxCorpAccount.getCorpId(),wxCorpAccount.getChatSecret());
                token=weAccessTokenDtoDto.getAccess_token();
                expires_in=weAccessTokenDtoDto.getExpires_in();
            }else if (WeConstans.WE_AGENT_ACCESS_TOKEN.equals(accessTokenKey)){
                WeAccessTokenDtoDto weAccessTokenDtoDto = accessTokenClient.getToken(wxCorpAccount.getCorpId(),wxCorpAccount.getAgentSecret());
                token=weAccessTokenDtoDto.getAccess_token();
                expires_in=weAccessTokenDtoDto.getExpires_in();
            }

            if(StringUtils.isNotEmpty(token)){
                redisCache.setCacheObject(accessTokenKey,token,expires_in.intValue(), TimeUnit.SECONDS);
                weAccessToken = token;
            }

        }

        return weAccessToken;
    }


    /**
     * 清空redis中的相关token
     */
    @Override
    public void removeToken() {
        redisCache.deleteObject(WeConstans.WE_COMMON_ACCESS_TOKEN);
        redisCache.deleteObject(WeConstans.WE_CONTACT_ACCESS_TOKEN);
        redisCache.deleteObject(WeConstans.WE_PROVIDER_ACCESS_TOKEN);
    }


}
