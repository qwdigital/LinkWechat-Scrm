package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.WeAccessTokenDtoDto;
import com.linkwechat.wecom.domain.dto.WeLoginUserInfoDto;

/**
 * @description: 获取企业微信Token相关
 * @author: HaoN
 * @create: 2020-08-26 14:33
 **/
public interface WeAccessTokenClient {



    /**
     *  获取token(常用token,联系人token)
     * @param corpId 企业id
     * @param corpSecret 应用的凭证密钥
     * @return
     */
    @Request(url = "/gettoken")
    WeAccessTokenDtoDto getToken(@Query("corpid") String corpId, @Query("corpsecret") String corpSecret);


    /**
     * 获取供应商token
     * @param corpid 服务商的corpid
     * @param provider_secret 服务商的secret，在服务商管理后台可见
     * @return
     */
    @Request(url = "/service/get_provider_token",
    type="POST")
    WeAccessTokenDtoDto getProviderToken(@Body("corpid") String corpid,@Body("provider_secret") String provider_secret);


    /**
     * 获取登录用户信息(扫码)
     * @param auth_code
     * @return
     */
    @Request(url = "/service/get_login_info",
            type = "POST"
    )
    WeLoginUserInfoDto getLoginInfo(@Body("auth_code") String auth_code);

}