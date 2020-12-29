package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeCustomerSeedMessage;
import com.linkwechat.wecom.domain.dto.message.CustomerMessagePushDto;

/**
 * 群发消息  子消息表(包括 文本消息、图片消息、链接消息、小程序消息) we_customer_seedMessage
 *
 * @author kewen
 * @date 2020-12-28
 */
public interface IWeCustomerSeedMessageService extends IService<WeCustomerSeedMessage> {
    /**
     * 各分类消息表
     *
     * @param customerMessagePushDto 群发消息
     * @param messageId              消息id
     */
     void saveSeedMessage(CustomerMessagePushDto customerMessagePushDto, long messageId);

}
