package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.vo.weixin.WxAuthUserInfoVo;
import com.linkwechat.domain.wecom.vo.weixin.WxTokenVo;
import com.linkwechat.fallback.WxAuthFallbackFactory;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author danmo
 * @description 企微授权接口
 * @date 2022/3/13 20:54
 **/
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = WxAuthFallbackFactory.class)
public interface WxAuthClient {


    @GetMapping("/weixin/auth/getToken")
    public AjaxResult<WxTokenVo> getToken(@ApiParam(value = "URL上的code参数", required = true) @RequestParam("code") String code);

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     */
    @GetMapping("/weixin/auth/getUserInfo")
    public AjaxResult<WxAuthUserInfoVo> getUserInfo(@ApiParam(value = "用户的唯一标识", required = true) @RequestParam("openId") String openId, @ApiParam(value = "语言版本", required = true) @RequestParam("lang") String lang);
}
