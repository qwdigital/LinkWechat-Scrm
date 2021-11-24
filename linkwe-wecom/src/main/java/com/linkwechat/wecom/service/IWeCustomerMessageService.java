package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeCustomerList;
import com.linkwechat.wecom.domain.WeCustomerMessage;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.dto.message.CustomerMessagePushDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群发消息  微信消息表service接口
 *
 * @author kewen
 * @date 2020-12-12
 */
public interface IWeCustomerMessageService extends IService<WeCustomerMessage> {

    /**
     *
     * @param messageId 微信消息表主键id
     * @param actualSend 实际发送消息数（客户对应多少人 客户群对应多个群）
     * @return int
     */
    int updateWeCustomerMessageActualSend( Long messageId, Integer actualSend);

    /**
     * 保存微信消息  WeCustomerMessage
     * @param messageId
     * @param messageOriginalId
     * @param customerMessagePushDto
     * @param size
     */
     void saveWeCustomerMessage(long messageId, long messageOriginalId, CustomerMessagePushDto customerMessagePushDto, int size,String content);

    /**
     *
     * @param messageId
     * @param msgIds
     * @throws JsonProcessingException
     */
     void updateMsgId(long messageId, List<String> msgIds) throws JsonProcessingException;

    /**
     * 发送消息
     *
     * @param customerMessagePushDto 消息信息
     * @param messageId
     * @throws JsonProcessingException
     */
    public void sendMessgae(CustomerMessagePushDto customerMessagePushDto, long messageId, List<WeCustomerList> customers, List<WeGroup> groups) throws JsonProcessingException;

}
