package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.vo.weixin.WxAuthUserInfoVo;
import com.linkwechat.domain.wecom.vo.weixin.WxTokenVo;
import com.linkwechat.fallback.QxAuthFallbackFactory;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author danmo
 * @description 企微授权接口
 * @date 2022/3/13 20:54
 **/
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QxAuthFallbackFactory.class)
public interface QxAuthClient {


    @GetMapping("/weixin/auth/getToken")
    public AjaxResult<WxTokenVo> getToken(@ApiParam(value = "URL上的code参数", required = true) @RequestParam("code") String code);

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     */
    @GetMapping("/weixin/auth/getUserInfo")
    public AjaxResult<WxAuthUserInfoVo> getUserInfo(@ApiParam(value = "用户的唯一标识", required = true) @RequestParam("openId") String openId, @ApiParam(value = "语言版本", required = true) @RequestParam("lang") String lang);

    /**
     * 获取接口调用权限
     * @param appId appId
     * @param secret 密钥
     * @return
     */
    @GetMapping("/weixin/auth/common/getToken")
    public AjaxResult<WxTokenVo> getCommonToken(@RequestParam("appId") String appId, @RequestParam("secret")  String secret);
}
