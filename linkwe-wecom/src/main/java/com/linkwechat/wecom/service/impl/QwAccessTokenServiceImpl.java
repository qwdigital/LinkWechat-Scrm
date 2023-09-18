package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeAgentInfo;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.wecom.vo.WeCorpTokenVo;
import com.linkwechat.service.IWeAgentInfoService;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.wecom.client.WeTokenClient;
import com.linkwechat.wecom.service.IQwAccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description: 微信token相关接口
 * @author: HaoN
 * @create: 2020-08-26 14:43
 **/
@Service
public class QwAccessTokenServiceImpl implements IQwAccessTokenService {


    @Resource
    private WeTokenClient weTokenClient;

    @Autowired
    private IWeCorpAccountService iWxCorpAccountService;


    @Autowired
    private RedisService redisService;

    @Autowired
    private IWeAgentInfoService weAgentInfoService;


    /**
     * 获取通用accessToken
     */
    @Override
    public String findCommonAccessToken(String corpId) {
        return findAccessToken(corpId, WeConstans.WE_COMMON_ACCESS_TOKEN);
    }


    /**
     * 获取外部联系人相关token
     *
     * @return
     */
    @Override
    public String findContactAccessToken(String corpId) {
        return findAccessToken(corpId, WeConstans.WE_CONTACT_ACCESS_TOKEN);
    }

    @Override
    public String findAddressBookAccessToken(String corpId) {
        return findAccessToken(corpId, WeConstans.WE_ADDRESS_BOOK_ACCESS_TOKEN);
    }

    /**
     * 获取服务商相关token
     *
     * @return
     */
    @Override
    public String findProviderAccessToken(String corpId) {
        return findAccessToken(corpId, WeConstans.WE_PROVIDER_ACCESS_TOKEN);
    }

    /**
     * 会话存档相关token
     *
     * @return
     */
    @Override
    public String findChatAccessToken(String corpId) {
        return findAccessToken(corpId, WeConstans.WE_CHAT_ACCESS_TOKEN);
    }


    @Override
    public String findKfAccessToken(String corpId) {
        return findAccessToken(corpId, WeConstans.WE_KF_ACCESS_TOKEN);
    }


    /**
     * 直播token
     * @param corpId
     * @return
     */
    @Override
    public String findLiveAccessToken(String corpId) {
        return findAccessToken(corpId, WeConstans.WE_LIVE_ACCESS_TOKEN);
    }

    @Override
    public String findBillAccessToken(String corpId) {
        return findAccessToken(corpId, WeConstans.WE_BILL_ACCESS_TOKEN);
    }


    //获取token
    private String findAccessToken(String corpId, String accessTokenKey) {
        String weAccessTokenKey = StringUtils.format(accessTokenKey, corpId);
        String weAccessToken = redisService.getCacheObject(weAccessTokenKey);
        //为空,请求微信服务器同时缓存到redis中
        if (StringUtils.isEmpty(weAccessToken)) {
            WeCorpAccount wxCorpAccount = iWxCorpAccountService.getCorpAccountByCorpId(corpId);
            if (Objects.isNull(wxCorpAccount)) {
                //返回错误异常，让用户绑定企业id相关信息
                throw new WeComException("无可用的corpid和secret");
            }
            WeCorpTokenVo weCorpTokenVo = null;
            if (WeConstans.WE_COMMON_ACCESS_TOKEN.equals(accessTokenKey)) {
                weCorpTokenVo = weTokenClient.getToken(wxCorpAccount.getCorpId(), wxCorpAccount.getAgentSecret());
            } else if (WeConstans.WE_CONTACT_ACCESS_TOKEN.equals(accessTokenKey)) {
                weCorpTokenVo = weTokenClient.getToken(wxCorpAccount.getCorpId(), wxCorpAccount.getContactSecret());
            } else if (WeConstans.WE_CHAT_ACCESS_TOKEN.equals(accessTokenKey)) {
                weCorpTokenVo = weTokenClient.getToken(wxCorpAccount.getCorpId(), wxCorpAccount.getChatSecret());
            } else if (WeConstans.WE_KF_ACCESS_TOKEN.equals(accessTokenKey)) {
                weCorpTokenVo = weTokenClient.getToken(wxCorpAccount.getCorpId(), wxCorpAccount.getKfSecret());
            } else if (WeConstans.WE_ADDRESS_BOOK_ACCESS_TOKEN.equals(accessTokenKey)) {
                weCorpTokenVo = weTokenClient.getToken(wxCorpAccount.getCorpId(), wxCorpAccount.getCorpSecret());
            } else if (WeConstans.WE_BILL_ACCESS_TOKEN.equals(accessTokenKey)) {
                weCorpTokenVo = weTokenClient.getToken(wxCorpAccount.getCorpId(), wxCorpAccount.getBillSecret());
            }else if(WeConstans.WE_LIVE_ACCESS_TOKEN.equals(accessTokenKey)){
                weCorpTokenVo = weTokenClient.getToken(wxCorpAccount.getCorpId(), wxCorpAccount.getLiveSecret());
            }

            if (Objects.nonNull(weCorpTokenVo) && StringUtils.isNotEmpty(weCorpTokenVo.getAccessToken())) {
                weAccessToken = weCorpTokenVo.getAccessToken();
                redisService.setCacheObject(weAccessTokenKey, weCorpTokenVo.getAccessToken(), weCorpTokenVo.getExpiresIn(), TimeUnit.SECONDS);
            }
        }
        return weAccessToken;
    }


    @Override
    public void removeCommonAccessToken(String corpId) {
        redisService.deleteObject(StringUtils.format(WeConstans.WE_COMMON_ACCESS_TOKEN, corpId));
    }


    @Override
    public void removeContactAccessToken(String corpId) {
        redisService.deleteObject(StringUtils.format(WeConstans.WE_CONTACT_ACCESS_TOKEN, corpId));
    }

    @Override
    public void removeChatAccessToken(String corpId) {
        redisService.deleteObject(StringUtils.format(WeConstans.WE_CHAT_ACCESS_TOKEN, corpId));
    }

    @Override
    public void removeKfAccessToken(String corpId) {
        redisService.deleteObject(StringUtils.format(WeConstans.WE_KF_ACCESS_TOKEN, corpId));
    }

    @Override
    public void removeAddressBookAccessToken(String corpId) {
        redisService.deleteObject(StringUtils.format(WeConstans.WE_ADDRESS_BOOK_ACCESS_TOKEN, corpId));
    }

    @Override
    public String findAgentAccessToken(String corpId, Integer agentId) {
        String weAgentTokenKey = StringUtils.format(WeConstans.WE_AGENT_ACCESS_TOKEN, corpId, agentId);
        String weAccessToken = redisService.getCacheObject(weAgentTokenKey);
        //为空,请求微信服务器同时缓存到redis中
        if (StringUtils.isEmpty(weAccessToken)) {
            WeAgentInfo weAgentInfo = weAgentInfoService.getAgentInfoByAgentId(agentId);
            if (Objects.isNull(weAgentInfo)) {
                throw new WeComException("无可用的应用");
            }
            WeCorpTokenVo weCorpTokenVo = weTokenClient.getToken(corpId, weAgentInfo.getSecret());

            if (Objects.nonNull(weCorpTokenVo) && StringUtils.isNotEmpty(weCorpTokenVo.getAccessToken())) {
                weAccessToken = weCorpTokenVo.getAccessToken();
                redisService.setCacheObject(weAgentTokenKey, weCorpTokenVo.getAccessToken(), weCorpTokenVo.getExpiresIn(), TimeUnit.SECONDS);
            }

        }
        return weAccessToken;
    }

    @Override
    public void removeAgentAccessToken(String corpId, Integer agentId) {
        redisService.deleteObject(StringUtils.format(WeConstans.WE_AGENT_ACCESS_TOKEN, corpId, agentId));
    }

    @Override
    public void removeBillAccessToken(String corpId) {
        redisService.deleteObject(StringUtils.format(WeConstans.WE_BILL_ACCESS_TOKEN, corpId));
    }

    @Override
    public void removeAllWeAccessToken(String corpId) {
        removeCommonAccessToken(corpId);
        removeContactAccessToken(corpId);
        removeChatAccessToken(corpId);
        removeKfAccessToken(corpId);
        removeAddressBookAccessToken(corpId);
        WeCorpAccount weCorpAccount = iWxCorpAccountService.getCorpAccountByCorpId(corpId);
        if(null != weCorpAccount && StringUtils.isNotEmpty(weCorpAccount.getAgentId())){
            removeAgentAccessToken(corpId,Integer.parseInt(weCorpAccount.getAgentId()));
        }

    }

    @Override
    public void removeLiveAccessToken(String corpId) {
        redisService.deleteObject(StringUtils.format(WeConstans.WE_LIVE_ACCESS_TOKEN, corpId));
    }
}
