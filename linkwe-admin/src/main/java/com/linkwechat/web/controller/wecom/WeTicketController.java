package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.wecom.TicketUtils;
import com.linkwechat.wecom.client.WeTicketClient;
import com.linkwechat.wecom.domain.WeH5TicketDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author danmo
 * @description h5 ticket校验接口
 * @date 2021/1/6 11:23
 **/
@Slf4j
@RequestMapping(value = "/wecom/ticket/")
@RestController
public class WeTicketController extends BaseController {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private WeTicketClient weTicketClient;


    /**
     * 获取企业的jsapi_ticket
     *
     * @param url JS接口页面的完整URL
     * @return
     */
    @Log(title = "获取企业的jsapi_ticket", businessType = BusinessType.OTHER)
    @GetMapping("/getAppTicket")
    public AjaxResult getAppTicket(String url,String agentId) {
        String ticketVaule = redisCache.getCacheObject(WeConstans.AppTicketKey+"::"+agentId);
        if (StringUtils.isEmpty(ticketVaule)) {
            WeH5TicketDto ticketRes = weTicketClient.getJsapiTicket(agentId);
            if (ticketRes != null && StringUtils.isNotEmpty(ticketRes.getTicket())) {
                redisCache.setCacheObject(WeConstans.AppTicketKey+"::"+agentId, ticketRes.getTicket(), ticketRes.getExpiresIn(), TimeUnit.SECONDS);
            }
            ticketVaule = ticketRes.getTicket();
        }
        return AjaxResult.success(TicketUtils.getSignatureMap(ticketVaule, url));
    }

    /**
     * 获取应用的jsapi_ticket
     *
     * @param url JS接口页面的完整URL
     * @return
     */
    @Log(title = "获取应用的jsapi_ticket", businessType = BusinessType.OTHER)
    @GetMapping("/getAgentTicket")
    public AjaxResult getAgentTicket(String url,String agentId) {
        String ticketVaule = redisCache.getCacheObject(WeConstans.AgentTicketKey+"::"+agentId);
        if (StringUtils.isEmpty(ticketVaule)) {
            WeH5TicketDto ticketRes = weTicketClient.getTicket(agentId);
            if (ticketRes != null && StringUtils.isNotEmpty(ticketRes.getTicket())) {
                redisCache.setCacheObject(WeConstans.AgentTicketKey+"::"+agentId, ticketRes.getTicket(), ticketRes.getExpiresIn(), TimeUnit.SECONDS);
            }
            ticketVaule = ticketRes.getTicket();
        }
        return AjaxResult.success(TicketUtils.getSignatureMap(ticketVaule, url));
    }


}
