package com.linkwechat.wecom.controller;

import com.dtflys.forest.annotation.Query;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.vo.weixin.WxAuthUserInfoVo;
import com.linkwechat.domain.wecom.vo.weixin.WxTokenVo;
import com.linkwechat.wecom.service.IWxAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author danmo
 * @description 微信授权controller
 * @date 2021/4/5 18:37
 **/
@Api(tags = "微信授权controller")
@RestController
@RequestMapping("/weixin/auth")
public class WxAuthController {

    @Autowired
    private IWxAuthService wxAuthService;

    /**
     * 通过code获取网页授权token
     */
    @ApiOperation(value = "通过code获取网页授权token", httpMethod = "GET")
    @GetMapping("/getToken")
    public AjaxResult<WxTokenVo> getToken(@ApiParam(value = "URL上的code参数", required = true) String code) {
        return AjaxResult.success(wxAuthService.getToken(code));
    }

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     */
    @ApiOperation(value = "拉取用户信息(需scope为 snsapi_userinfo)", httpMethod = "GET")
    @GetMapping("/getUserInfo")
    public AjaxResult<WxAuthUserInfoVo> getUserInfo(@ApiParam(value = "用户的唯一标识", required = true) String openId, @ApiParam(value = "语言版本", required = true) String lang) {
        return AjaxResult.success(wxAuthService.getUserInfo(openId, lang));
    }


    /**
     * 获取接口调用凭据
     */
    @ApiOperation(value = "获取接口调用凭据", httpMethod = "GET")
    @GetMapping("/common/getToken")
    public AjaxResult<WxTokenVo> getCommonToken(@RequestParam("appId") String appId, @RequestParam("secret")  String secret) {
        return AjaxResult.success(wxAuthService.getCommonToken(appId,secret));
    }

}
