package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.weixin.WxJumpWxaQuery;
import com.linkwechat.domain.wecom.vo.weixin.WxJumpWxaVo;
import com.linkwechat.fegin.QxAppletClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 微信小程序
 * @date 2022/1/5 20:57
 **/
@Component
@Slf4j
public class QxAppletFallbackFactory implements QxAppletClient {


    @Override
    public AjaxResult<WxJumpWxaVo> generateScheme(WxJumpWxaQuery query) {
        return null;
    }
}
