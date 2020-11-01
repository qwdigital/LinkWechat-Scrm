package com.linkwechat.wecom.service;

/**
 * @description: 微信token相关接口
 * @author: My
 * @create: 2020-08-26 14:43
 **/
public interface IWeAccessTokenService {

   public String  findCommonAccessToken();


   public String findContactAccessToken();


   public String findProviderAccessToken();
}