package com.linkwechat.wecom.service.impl;

import com.linkwechat.wecom.client.WeAccessTokenClient;
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
    private RedisTemplate redisTemplate;

    /**
     * 获取accessToken
     */
    @Override
    public void findToken() {
//        WeCorpAccount wxCorpAccount
//                = iWxCorpAccountService.findValidWxCorpAccount();
//        if(null == wxCorpAccount){
//             //返回错误异常，让用户绑定企业id相关信息
//        }
//        WeAccessTokenDtoDto accessToken
//                = accessTokenClient.getToken(wxCorpAccount.getCorpId(), wxCorpAccount.getCorpSecret());
//        if(null != accessToken){
//            //token存入redis中(清空原有的token)
////            redisTemplate.opsForValue().set();
//
//
//        }




    }





}
