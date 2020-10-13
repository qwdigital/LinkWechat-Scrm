package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.WeAccessTokenDtoDto;

/**
 * @description: 获取企业微信Token相关
 * @author: HaoN
 * @create: 2020-08-26 14:33
 **/
public interface WeAccessTokenClient {



    /**
     *  获取token
     * @param corpId 企业id
     * @param corpSecret 应用的凭证密钥
     * @return
     */
    @Request(url = "/gettoken")
    WeAccessTokenDtoDto getToken(@Query("corpid") String corpId, @Query("corpsecret") String corpSecret);

}