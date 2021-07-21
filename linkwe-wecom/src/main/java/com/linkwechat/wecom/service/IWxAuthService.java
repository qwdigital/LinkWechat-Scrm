package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.weixin.dto.WxAuthUserInfoDto;
import com.linkwechat.wecom.domain.weixin.dto.WxTokenDto;

/**
 * @author danmo
 * @description
 * @date 2021/4/5 22:45
 **/
public interface IWxAuthService {
    WxTokenDto getToken(String code,String openId);

    WxAuthUserInfoDto getUserInfo(String openId, String lang);
}
