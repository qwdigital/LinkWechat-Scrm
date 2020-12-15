package com.linkwechat.wecom.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkwechat.wecom.domain.dto.message.CustomerMessagePushDto;

/**
 * @description: 群发消息服务类
 * @author: KeWen
 * @create: 2020-12-01 20:23
 **/
public interface IWeCustomerMessagePushService {

    /**
     * 新增群发消息发送
     *
     *@param customerMessagePushDto 原始数据信息
     */
    public void  addWeCustomerMessagePush(CustomerMessagePushDto customerMessagePushDto) throws JsonProcessingException;

}
