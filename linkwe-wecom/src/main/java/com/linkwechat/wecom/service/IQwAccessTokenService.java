package com.linkwechat.wecom.service;


/**
 * @description: 微信token相关接口
 * @author: My
 * @create: 2020-08-26 14:43
 **/
public interface IQwAccessTokenService {

    String  findCommonAccessToken(String corpId);

    String findContactAccessToken(String corpId);

    String findAddressBookAccessToken(String corpId);

    String findProviderAccessToken(String corpId);

    String findChatAccessToken(String corpId);

    String findKfAccessToken(String corpId);

    String findLiveAccessToken(String corpId);
    /**
     * 对外收款Token
     *
     * @param corpId
     * @return
     */
     String findBillAccessToken(String corpId);


    void removeCommonAccessToken(String corpId);

    void removeContactAccessToken(String corpId);

    void removeChatAccessToken(String corpId);

    void removeKfAccessToken(String corpId);

    void removeAddressBookAccessToken(String corpId);

    String findAgentAccessToken(String corpId, Integer agentId);

   void removeAgentAccessToken(String corpId, Integer agentId);

   void removeAllWeAccessToken(String corpId);

    void removeLiveAccessToken(String corpId);

    /**
     * 移除对外收款Token
     */
     void removeBillAccessToken(String corpId);

}