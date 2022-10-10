package com.linkwechat.gateway.service;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.exception.user.CaptchaException;

import java.io.IOException;

/**
 * 验证码处理
 *
 * @author leejoker
 */
public interface ValidateCodeService {
    /**
     * 生成验证码
     */
    public AjaxResult createCapcha() throws IOException, CaptchaException;

    /**
     * 校验验证码
     */
    public void checkCapcha(String key, String value) throws CaptchaException;
}
