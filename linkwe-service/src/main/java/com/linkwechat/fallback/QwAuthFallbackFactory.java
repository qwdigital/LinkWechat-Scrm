package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.WeThirdLoginQuery;
import com.linkwechat.domain.wecom.query.department.WeDeptQuery;
import com.linkwechat.domain.wecom.query.third.auth.*;
import com.linkwechat.domain.wecom.query.user.WeLoginUserDetailQuery;
import com.linkwechat.domain.wecom.query.user.WeUserListQuery;
import com.linkwechat.domain.wecom.query.user.WeUserQuery;
import com.linkwechat.domain.wecom.vo.WeCorpTokenVo;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.third.auth.WeAuthInfoVo;
import com.linkwechat.domain.wecom.vo.third.auth.WeGetCustomizedAuthUrlVo;
import com.linkwechat.domain.wecom.vo.third.auth.WeGetQrCodeVo;
import com.linkwechat.domain.wecom.vo.third.auth.WePreAuthCodeVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptVo;
import com.linkwechat.domain.wecom.vo.third.auth.*;
import com.linkwechat.domain.wecom.vo.user.WeLoginUserDetailVo;
import com.linkwechat.domain.wecom.vo.user.WeLoginUserVo;
import com.linkwechat.domain.wecom.vo.user.WeThirdLoginUserVo;
import com.linkwechat.domain.wecom.vo.user.WeUserListVo;
import com.linkwechat.fegin.QwAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 企微授权
 * @date 2022/3/13 20:57
 **/
@Component
@Slf4j
public class QwAuthFallbackFactory implements QwAuthClient {

    @Override
    public AjaxResult<WeCorpTokenVo> getSuiteToken(String suiteId) {
        return null;
    }

    @Override
    public AjaxResult<WePreAuthCodeVo> getPreAuthCode(WeBaseQuery query) {
        return AjaxResult.success(null);
    }

    @Override
    public AjaxResult<WeResultVo> setSessionInfo(WeSetSessionInfoQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeAuthInfoVo> getPermanentCode(WeGetPermanentCodeQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeAuthInfoVo> getAuthInfo(WeGetAuthInfoQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeCorpTokenVo> getCorpToken(WeBaseQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeCorpTokenVo> getAdminList(WeAuthAdminQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeGetQrCodeVo> getAppQrCode(WeGetAppQrCodeQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeGetCustomizedAuthUrlVo> getCustomizedAuthUrl(WeGetCustomizedAuthUrlQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeThirdLoginUserVo> getThirdLoginInfo(WeThirdLoginQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeLoginUserVo> getUserInfo3rd(WeUserQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeDeptVo> getDeptList(WeDeptQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeUserListVo> getSimpleList(WeUserListQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeExtensionRegisterVo> getRegisterCode(WeExtensionRegisterQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeLoginUserDetailVo> getuserdetail3rd(WeLoginUserDetailQuery query) {
        return null;
    }
}
