package com.linkwechat.wecom.service;

import com.linkwechat.common.core.domain.entity.WeCorpAccount;

/**
 * @description: 微信token相关接口
 * @author: My
 * @create: 2020-08-26 14:43
 **/
public interface IWeAccessTokenService {

   public String  findCommonAccessToken();


   public String findContactAccessToken();


   public String findProviderAccessToken();

   public String findChatAccessToken();


   public String findThirdAppAccessToken();






   public void removeToken(WeCorpAccount wxCorpAccount);

   public void removeCommonAccessToken();
   public void removeContactAccessToken();

   public void removeChatAccessToken();

   public void removeThirdAppAccessToken(String agentId);
}