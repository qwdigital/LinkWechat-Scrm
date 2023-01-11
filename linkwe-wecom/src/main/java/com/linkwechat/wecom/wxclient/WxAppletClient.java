package com.linkwechat.wecom.wxclient;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.linkwechat.domain.wecom.query.weixin.WxJumpWxaQuery;
import com.linkwechat.domain.wecom.vo.weixin.WxJumpWxaVo;
import com.linkwechat.domain.wecom.vo.weixin.WxTokenVo;
import com.linkwechat.wecom.interceptor.WxAppletAccessTokenInterceptor;

/**
 * @author danmo
 * @description 微信小程序
 * @date 2023/1/5 17:11
 **/
@BaseRequest(baseURL = "${wxAppletServerUrl}", contentType = "application/json")
public interface WxAppletClient {
    /**
     * 获取 scheme 码
     *
     * @param query 入参见文档
     * @return
     */
    @Post(url = "/generatescheme", interceptor = WxAppletAccessTokenInterceptor.class)
    WxJumpWxaVo generateScheme(@JSONBody WxJumpWxaQuery query);


}
