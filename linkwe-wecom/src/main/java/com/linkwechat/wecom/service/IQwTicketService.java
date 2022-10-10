package com.linkwechat.wecom.service;

import com.linkwechat.domain.wecom.query.WeBaseQuery;

/**
 * @author danmo
 * @description h5 ticket校验接口
 * @date 2021/1/6 11:23
 **/
public interface IQwTicketService {

    String getCorpTicket(WeBaseQuery query);

    String getAgentTicket(WeBaseQuery query);

    String getWxTicket();
}
