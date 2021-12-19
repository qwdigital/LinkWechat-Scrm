package com.linkwechat.wecom.service.impl;

import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeAccessTokenClient;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import com.linkwechat.wecom.domain.dto.WeAccessTokenDto;
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





    @Autowired
    private RuoYiConfig ruoYiConfig;



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
     * 获取应用所需要的token(移除weapp,直接从account表中获取相关信息)
     * @return
     */
    @Override
    public String findThirdAppAccessToken() {


        //首先表中查询提醒的appid
        WeCorpAccount weCorpAccount = iWxCorpAccountService.findValidWeCorpAccount();
        if(null == weCorpAccount){
            throw new WeComException("无可用的corpid和secret");
        }

        if(StringUtils.isEmpty(weCorpAccount.getAgentId())&&StringUtils.isEmpty(weCorpAccount.getAgentSecret())){
            throw new WeComException("请确保应用id与应用密钥可用");
        }
        String token=redisCache.getCacheObject(WeConstans.WE_THIRD_APP_TOKEN+"::"+weCorpAccount.getAgentId());


        return StringUtils.isNotEmpty(token)
                ?token:findThirdAppAccessToken(weCorpAccount);

    }


    private String findThirdAppAccessToken(WeCorpAccount weCorpAccount){


        WeAccessTokenDto weAccessTokenDto
                = accessTokenClient.getToken(weCorpAccount.getCorpId(),weCorpAccount.getAgentSecret());

        if(StringUtils.isNotEmpty(weAccessTokenDto.getAccessToken())){

            redisCache.setCacheObject(WeConstans.WE_THIRD_APP_TOKEN+"::"+weCorpAccount.getAgentId(), weAccessTokenDto.getAccessToken(), weAccessTokenDto.getExpiresIn().intValue(), TimeUnit.SECONDS);
        }


        return weAccessTokenDto.getAccessToken();

    }




    private String findAccessToken(String accessTokenKey){

        return ruoYiConfig.isStartTenant()?findAccessTokenForTenant(accessTokenKey):findAccessTokenForNoTenant(accessTokenKey);

    }



    //多租户环境下获取token
    private String findAccessTokenForTenant(String accessTokenKey){


        WeCorpAccount wxCorpAccount
                = SecurityUtils.getLoginUser().getUser().getWeCorpAccount();

        if(null == wxCorpAccount){
            //返回错误异常，让用户绑定企业id相关信息
            throw new WeComException("无可用的corpid和secret");
        }


        String  weAccessToken =redisCache.getCacheObject(accessTokenKey+"::"+wxCorpAccount.getCorpId());



        //为空,请求微信服务器同时缓存到redis中
        if(StringUtils.isEmpty(weAccessToken)){

            String token="";
            Long expires_in=null;
            if(WeConstans.WE_COMMON_ACCESS_TOKEN.equals(accessTokenKey) || WeConstans.WE_CONTACT_ACCESS_TOKEN.equals(accessTokenKey)){
                WeAccessTokenDto weAccessTokenDto = accessTokenClient.getToken(wxCorpAccount.getCorpId(),
                        WeConstans.WE_COMMON_ACCESS_TOKEN.equals(accessTokenKey) ? wxCorpAccount.getCorpSecret() : wxCorpAccount.getContactSecret());
                token= weAccessTokenDto.getAccessToken();
                expires_in= weAccessTokenDto.getExpiresIn();

            }else if (WeConstans.WE_CHAT_ACCESS_TOKEN.equals(accessTokenKey)){
                WeAccessTokenDto weAccessTokenDto = accessTokenClient.getToken(wxCorpAccount.getCorpId(),wxCorpAccount.getChatSecret());
                token= weAccessTokenDto.getAccessToken();
                expires_in= weAccessTokenDto.getExpiresIn();
            }else if (WeConstans.WE_AGENT_ACCESS_TOKEN.equals(accessTokenKey)){
                WeAccessTokenDto weAccessTokenDto = accessTokenClient.getToken(wxCorpAccount.getCorpId(),wxCorpAccount.getAgentSecret());
                token= weAccessTokenDto.getAccessToken();
                expires_in= weAccessTokenDto.getExpiresIn();
            }

            if(StringUtils.isNotEmpty(token)){
                redisCache.setCacheObject(accessTokenKey+"::"+wxCorpAccount.getCorpId(),token,expires_in.intValue(), TimeUnit.SECONDS);
                weAccessToken = token;
            }

        }
        return weAccessToken;


    }


    //非多租户环境下获取token
    private String findAccessTokenForNoTenant(String accessTokenKey){
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
                    WeAccessTokenDto weAccessTokenDto = accessTokenClient.getToken(wxCorpAccount.getCorpId(),
                            WeConstans.WE_COMMON_ACCESS_TOKEN.equals(accessTokenKey) ? wxCorpAccount.getCorpSecret() : wxCorpAccount.getContactSecret());
                    token= weAccessTokenDto.getAccessToken();
                    expires_in= weAccessTokenDto.getExpiresIn();
                }else if (WeConstans.WE_CHAT_ACCESS_TOKEN.equals(accessTokenKey)){
                    WeAccessTokenDto weAccessTokenDto = accessTokenClient.getToken(wxCorpAccount.getCorpId(),wxCorpAccount.getChatSecret());
                    token= weAccessTokenDto.getAccessToken();
                    expires_in= weAccessTokenDto.getExpiresIn();
                }else if (WeConstans.WE_AGENT_ACCESS_TOKEN.equals(accessTokenKey)){
                    WeAccessTokenDto weAccessTokenDto = accessTokenClient.getToken(wxCorpAccount.getCorpId(),wxCorpAccount.getAgentSecret());
                    token= weAccessTokenDto.getAccessToken();
                    expires_in= weAccessTokenDto.getExpiresIn();
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
    public void removeToken(WeCorpAccount wxCorpAccount) {
        if(ruoYiConfig.isStartTenant()){
            redisCache.deleteObject(WeConstans.WE_COMMON_ACCESS_TOKEN+"::"+wxCorpAccount.getAgentId());
            redisCache.deleteObject(WeConstans.WE_CONTACT_ACCESS_TOKEN+"::"+wxCorpAccount.getAgentId());
            redisCache.deleteObject(WeConstans.WE_PROVIDER_ACCESS_TOKEN+"::"+wxCorpAccount.getAgentId());
        }else{
            redisCache.deleteObject(WeConstans.WE_COMMON_ACCESS_TOKEN);
            redisCache.deleteObject(WeConstans.WE_CONTACT_ACCESS_TOKEN);
            redisCache.deleteObject(WeConstans.WE_PROVIDER_ACCESS_TOKEN);
        }

    }

    @Override
    public void removeCommonAccessToken() {
        redisCache.deleteObject(WeConstans.WE_COMMON_ACCESS_TOKEN);
    }


    @Override
    public void removeContactAccessToken() {
        redisCache.deleteObject(WeConstans.WE_CONTACT_ACCESS_TOKEN);
    }

    @Override
    public void removeChatAccessToken() {
        redisCache.deleteObject(WeConstans.WE_CHAT_ACCESS_TOKEN);
    }

    @Override
    public void removeThirdAppAccessToken(String agentId) {
        redisCache.deleteObject(WeConstans.WE_THIRD_APP_TOKEN+"::"+agentId);
    }
}
