package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.vo.weixin.WxAuthUserInfoVo;
import com.linkwechat.domain.wecom.vo.weixin.WxTokenVo;
import com.linkwechat.fegin.QxAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 微信授权
 * @date 2022/3/13 20:57
 **/
@Component
@Slf4j
public class QxAuthFallbackFactory implements QxAuthClient {


    @Override
    public AjaxResult<WxTokenVo> getToken(String code) {
        return null;
    }

    @Override
    public AjaxResult<WxAuthUserInfoVo> getUserInfo(String openId, String lang) {
        return null;
    }

    @Override
    public AjaxResult<WxTokenVo> getCommonToken(String appId, String secret) {
        return null;
    }
}
