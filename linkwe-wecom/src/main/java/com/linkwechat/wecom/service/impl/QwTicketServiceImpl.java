package com.linkwechat.wecom.service.impl;

import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.vo.third.auth.WeTicketVo;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.wecom.client.WeTicketClient;
import com.linkwechat.wecom.service.IQwTicketService;
import com.linkwechat.wecom.wxclient.WxCommonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/3/23 21:11
 */
@Service
public class QwTicketServiceImpl implements IQwTicketService {
    @Resource
    private WeTicketClient weTicketClient;

    @Autowired
    private RedisService redisService;

    @Autowired
    private WxCommonClient wxCommonClient;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    private final String corpTicketKey = "corpTicket:get:{}";
    private final String agentTicketKey = "agentTicket:get:{}";
    private final String wxTicketKey="wxTicketKey:get:{}:{}";


    @Override
    public String getCorpTicket(WeBaseQuery query) {
        String key = StringUtils.format(corpTicketKey, query.getCorpid());
        String ticketVaule = (String) redisService.getCacheObject(key);
        if (StringUtils.isEmpty(ticketVaule)) {
            WeTicketVo corpTicket = weTicketClient.getCorpTicket(query);
            redisService.setCacheObject(key, corpTicket.getTicket(), corpTicket.getExpiresIn().intValue(), TimeUnit.SECONDS);
            ticketVaule = corpTicket.getTicket();
        }
        return ticketVaule;
    }

    @Override
    public String getAgentTicket(WeBaseQuery query) {
        String key = StringUtils.format(agentTicketKey, query.getCorpid());
        String ticketVaule = (String) redisService.getCacheObject(key);
        if (StringUtils.isEmpty(ticketVaule)) {
            WeTicketVo appTicket = weTicketClient.getAppTicket(query);
            redisService.setCacheObject(key, appTicket.getTicket(), appTicket.getExpiresIn().intValue(), TimeUnit.SECONDS);
            ticketVaule = appTicket.getTicket();
        }
        return ticketVaule;
    }

    @Override
    public String getWxTicket() {
        WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
        String key = StringUtils.format(wxTicketKey, weCorpAccount.getCorpId(), weCorpAccount.getWxAppId());
        String ticketVaule = (String) redisService.getCacheObject(key);
        if (StringUtils.isEmpty(ticketVaule)) {
            WeTicketVo weTicketVo = wxCommonClient.getTicket("jsapi");
            redisService.setCacheObject(key, weTicketVo.getTicket(), weTicketVo.getExpiresIn().intValue(), TimeUnit.SECONDS);
            ticketVaule = weTicketVo.getTicket();
        }
        return ticketVaule;
    }
}
