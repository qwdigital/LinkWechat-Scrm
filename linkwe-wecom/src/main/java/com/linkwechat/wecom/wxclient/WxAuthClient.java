package com.linkwechat.wecom.wxclient;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.weixin.dto.WxTokenDto;
import com.linkwechat.wecom.domain.weixin.dto.WxAuthUserInfoDto;
import com.linkwechat.wecom.interceptor.WeiXinAuthInterceptor;

/**
 * @author danmo
 * @description
 * @date 2021/4/5 15:47
 **/
@BaseRequest(baseURL = "${wxServerUrl}${wxPrefix}",contentType = "application/json",interceptor = WeiXinAuthInterceptor.class)
public interface WxAuthClient {
    /**
     * 获取微信授权token
     *
     * @param appId     公众号的唯一标识
     * @param secret    密钥
     * @param code      302重定向code
     * @param grantType 填写为authorization_code
     * @return
     */
    @Request(url = "/oauth2/access_token", type = "GET")
    WxTokenDto getAuthToken(@Query("appid") String appId, @Query("secret") String secret,
                            @Query("code") String code, @Query("grant_type") String grantType);

    /**
     * 刷新access_token
     *
     * @param appId     公众号的唯一标识
     * @param grantType 填写为refresh_token
     * @param refreshToken 填写通过access_token获取到的refresh_token参数
     * @return
     */
    @Request(url = "/oauth2/refresh_token", type = "GET")
    WxTokenDto refreshToken(@Query("appid") String appId, @Query("grant_type") String grantType,
                            @Query("refresh_token") String refreshToken);

    /**
     * 检验授权凭证（access_token）是否有效
     * @param openId 用户的唯一标识
     * @return
     */
    @Request(url = "/auth", type = "GET")
    WxTokenDto auth(@Query("openid") String openId);


    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     * @param openId 用户的唯一标识
     * @param lang 返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     * @return
     */
    @Request(url = "/userinfo", type = "GET")
    WxAuthUserInfoDto getUserInfo(@Query("openid") String openId, @Query("lang") String lang);

}
