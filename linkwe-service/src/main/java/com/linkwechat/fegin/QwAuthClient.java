package com.linkwechat.fegin;

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
import com.linkwechat.fallback.QwAuthFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author danmo
 * @description 企微授权接口
 * @date 2022/3/13 20:54
 **/
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwAuthFallbackFactory.class)
public interface QwAuthClient {

    /**
     * 获取第三方应用凭证
     *
     * @param suiteId 应用ID
     * @return
     */
    @GetMapping("/auth/getSuiteToken")
    public AjaxResult<WeCorpTokenVo> getSuiteToken(String suiteId);

    /**
     * 获取预授权码
     *
     * @param query
     * @return
     */
    @PostMapping("/auth/getPreAuthCode")
    public AjaxResult<WePreAuthCodeVo> getPreAuthCode(@RequestBody WeBaseQuery query);

    /**
     * 设置授权配置
     *
     * @param query
     * @return
     */
    @PostMapping("/auth/setSessionInfo")
    public AjaxResult<WeResultVo> setSessionInfo(@RequestBody WeSetSessionInfoQuery query);

    /**
     * 获取企业永久授权码
     *
     * @param query
     * @return
     */
    @PostMapping("/auth/getPermanentCode")
    public AjaxResult<WeAuthInfoVo> getPermanentCode(@RequestBody WeGetPermanentCodeQuery query);

    /**
     * 获取企业授权信息
     *
     * @param query
     * @return
     */
    @PostMapping("/auth/getAuthInfo")
    public AjaxResult<WeAuthInfoVo> getAuthInfo(@RequestBody WeGetAuthInfoQuery query);

    /**
     * 获取企业凭证
     *
     * @param query
     * @return
     */
    @PostMapping("/auth/getCorpToken")
    public AjaxResult<WeCorpTokenVo> getCorpToken(@RequestBody WeBaseQuery query);

    /**
     * 获取应用的管理员列表
     *
     * @param query
     * @return
     */
    @PostMapping("/auth/getAdminList")
    public AjaxResult<WeCorpTokenVo> getAdminList(@RequestBody WeAuthAdminQuery query);

    /**
     * 获取应用二维码
     *
     * @param query
     * @return
     */
    @PostMapping("/auth/getAppQrCode")
    public AjaxResult<WeGetQrCodeVo> getAppQrCode(@RequestBody WeGetAppQrCodeQuery query);

    /**
     * 获取带参授权链接
     *
     * @param query
     * @return
     */
    @PostMapping("/auth/getCustomizedAuthUrl")
    public AjaxResult<WeGetCustomizedAuthUrlVo> getCustomizedAuthUrl(@RequestBody WeGetCustomizedAuthUrlQuery query);

    /**
     * 获取第三方客户的员工信息
     *
     * @param query
     * @return
     */
    @PostMapping("/auth/getThirdLoginInfo")
    public AjaxResult<WeThirdLoginUserVo> getThirdLoginInfo(@RequestBody WeThirdLoginQuery query);

    /*
     *  获取第三方客户的员工信息
     * @param authCode
     * @return
     */
    @PostMapping("/auth/getUserInfo3rd")
    public AjaxResult<WeLoginUserVo> getUserInfo3rd(@RequestBody WeUserQuery query);

    /**
     * 获取部门列表
     *
     * @param query
     * @return
     */
    @PostMapping("/dept/list")
    AjaxResult<WeDeptVo> getDeptList(@RequestBody WeDeptQuery query);

    /**
     * 获取部门成员
     *
     * @param query
     * @return
     */
    @GetMapping("/user/getSimpleList")
    AjaxResult<WeUserListVo> getSimpleList(@RequestBody WeUserListQuery query);

    /**
     * 获取企业微信推广code
     * @param query
     * @return
     */
    @PostMapping("/auth/getRegisterCode")
    AjaxResult<WeExtensionRegisterVo> getRegisterCode(@RequestBody WeExtensionRegisterQuery query);


    /**
     * 获取访问用户敏感信息
     * @param query
     * @return
     */
    @PostMapping("/auth/getuserdetail3rd")
    AjaxResult<WeLoginUserDetailVo> getuserdetail3rd(@RequestBody WeLoginUserDetailQuery query);
}
