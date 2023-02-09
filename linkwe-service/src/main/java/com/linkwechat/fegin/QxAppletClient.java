package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.weixin.WxJumpWxaQuery;
import com.linkwechat.domain.wecom.vo.weixin.WxJumpWxaVo;
import com.linkwechat.fallback.QxAppletFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author danmo
 * @description 微信小程序接口
 * @date 2022/1/5 20:54
 **/
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QxAppletFallbackFactory.class)
public interface QxAppletClient {


    @PostMapping("/weixin/applet/generateScheme")
    public AjaxResult<WxJumpWxaVo> generateScheme(@RequestBody WxJumpWxaQuery query);

}
