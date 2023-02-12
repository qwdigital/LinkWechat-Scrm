package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.weixin.WxJumpWxaQuery;
import com.linkwechat.domain.wecom.vo.weixin.WxJumpWxaVo;
import com.linkwechat.domain.wecom.vo.weixin.WxTokenVo;
import com.linkwechat.wecom.wxclient.WxAppletClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author danmo
 * @description 微信小程序controller
 * @date 2022/1/5 18:37
 **/
@Api(tags = "微信授权controller")
@RestController
@RequestMapping("/weixin/applet")
public class WxAppletController {

    @Resource
    private WxAppletClient wxAppletClient;

    /**
     * 获取 scheme 码
     */
    @ApiOperation(value = "获取 scheme 码", httpMethod = "POST")
    @PostMapping("/generateScheme")
    public AjaxResult<WxJumpWxaVo> generateScheme(@RequestBody WxJumpWxaQuery query) {
        return AjaxResult.success(wxAppletClient.generateScheme(query));
    }


}
