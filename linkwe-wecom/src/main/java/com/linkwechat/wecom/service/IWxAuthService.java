package com.linkwechat.wecom.service;

import com.linkwechat.domain.wecom.vo.weixin.WxAuthUserInfoVo;
import com.linkwechat.domain.wecom.vo.weixin.WxTokenVo;

/**
 * @author danmo
 * @description
 * @date 2021/4/5 22:45
 **/
public interface IWxAuthService {
    WxTokenVo getToken(String code);

    WxAuthUserInfoVo getUserInfo(String openId, String lang);

    WxTokenVo getCommonToken(String appId, String secret);
}
