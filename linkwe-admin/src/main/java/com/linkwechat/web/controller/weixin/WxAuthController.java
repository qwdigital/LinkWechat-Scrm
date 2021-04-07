package com.linkwechat.web.controller.weixin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.web.controller.common.CommonController;
import com.linkwechat.wecom.domain.weixin.dto.WxAuthUserInfoDto;
import com.linkwechat.wecom.domain.weixin.dto.WxTokenDto;
import com.linkwechat.wecom.service.IWxAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author danmo
 * @description 微信授权controller
 * @date 2021/4/5 18:37
 **/
@Api("微信授权controller")
@Slf4j
@RestController
@RequestMapping("/weixin/auth")
public class WxAuthController extends CommonController {

    @Autowired
    private IWxAuthService wxAuthService;

    /**
     * 通过code获取网页授权token
     */
    @ApiOperation(value = "通过code获取网页授权token",httpMethod = "GET")
    @GetMapping("/getToken")
    public AjaxResult<WxTokenDto> getToken(@ApiParam(value = "URL上的code参数",required = true) String code, @ApiParam(value = "用户的唯一标识",required = false) String openId) {
        return AjaxResult.success(wxAuthService.getToken(code,openId));
    }

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     */
    @ApiOperation(value = "拉取用户信息(需scope为 snsapi_userinfo)",httpMethod = "GET")
    @GetMapping("/getUserInfo")
    public AjaxResult<WxAuthUserInfoDto> getUserInfo(@ApiParam(value = "用户的唯一标识",required = true) String openId, @ApiParam(value = "语言版本",required = true) String lang) {
        return AjaxResult.success(wxAuthService.getUserInfo(openId,lang));
    }
}
