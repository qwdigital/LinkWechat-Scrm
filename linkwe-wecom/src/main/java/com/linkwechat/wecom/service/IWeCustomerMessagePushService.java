package com.linkwechat.wecom.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkwechat.wecom.domain.dto.message.CustomerMessagePushDto;
import com.linkwechat.wecom.domain.vo.CustomerMessagePushVo;

import java.text.ParseException;
import java.util.List;

/**
 * @description: 群发消息服务类
 * @author: KeWen
 * @create: 2020-12-01 20:23
 **/
public interface IWeCustomerMessagePushService {

    /**
     * 新增群发消息发送
     *
     *@param customerMessagePush 原始数据信息
     */
    public void  addWeCustomerMessagePush(CustomerMessagePushDto customerMessagePush) throws JsonProcessingException, ParseException, CloneNotSupportedException;

    /**
     * 群发消息列表
     *
     * @param sender 创建人
     * @param content 内容
     * @param pushType 群发类型
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return {@link CustomerMessagePushVo}s
     */
    public List<CustomerMessagePushVo> customerMessagePushs(String sender,String content,String pushType,String beginTime,String endTime);


    /**
     * 消息推送提醒
     * @param messageRemindContent
     * @param toUserId
     */
    public void messagePushRemind(String messageRemindContent,String toUserId);

}
