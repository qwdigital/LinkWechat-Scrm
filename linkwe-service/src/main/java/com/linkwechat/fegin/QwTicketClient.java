package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.fallback.QwTicketFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author danmo
 * @description 企微企业接口
 * @date 2022/3/13 20:54
 **/
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwTicketFallbackFactory.class)
public interface QwTicketClient {

    /**
     * 获取企业的jsapi_ticket
     *
     * @param query JS接口页面的完整URL
     * @return
     */
    @PostMapping("/ticket/getCorpTicket")
    public AjaxResult<String> getCorpTicket(@RequestBody WeBaseQuery query);

    /**
     * 获取应用的jsapi_ticket
     *
     * @param query JS接口页面的完整URL
     * @return
     */
    @PostMapping("/ticket/getAgentTicket")
    public AjaxResult<String> getAgentTicket(@RequestBody WeBaseQuery query);

    /**
     * 获取微信的ticket
     * @return
     */
    @GetMapping("/ticket/getWxTicket")
    public AjaxResult<String> getWxTicket();
}
