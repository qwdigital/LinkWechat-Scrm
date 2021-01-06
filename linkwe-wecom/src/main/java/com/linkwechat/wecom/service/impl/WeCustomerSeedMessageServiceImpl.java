package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.wecom.domain.WeCustomerSeedMessage;
import com.linkwechat.wecom.domain.dto.message.CustomerMessagePushDto;
import com.linkwechat.wecom.mapper.WeCustomerSeedMessageMapper;
import com.linkwechat.wecom.service.IWeCustomerSeedMessageService;
import org.springframework.stereotype.Service;
/**
 * 群发消息  子消息表(包括 文本消息、图片消息、链接消息、小程序消息) we_customer_seedMessage
 *
 * @author kewen
 * @date 2020-12-28
 */
@Service
public class WeCustomerSeedMessageServiceImpl extends ServiceImpl<WeCustomerSeedMessageMapper, WeCustomerSeedMessage> implements IWeCustomerSeedMessageService {

    @Override
    public void saveSeedMessage(CustomerMessagePushDto customerMessagePushDto, long messageId) {
        WeCustomerSeedMessage customerSeedMessage=new WeCustomerSeedMessage();
        customerSeedMessage.setSeedMessageId(SnowFlakeUtil.nextId());
        customerSeedMessage.setMessageId(messageId);
        //文本消息
        customerSeedMessage.setContent(customerMessagePushDto.getTextMessage()!=null ?customerMessagePushDto.getTextMessage().getContent():null);
        //图片消息
        customerSeedMessage.setMediaId(customerMessagePushDto.getImageMessage()!=null ?customerMessagePushDto.getImageMessage().getMedia_id():null);
        customerSeedMessage.setPicUrl(customerMessagePushDto.getImageMessage()!=null ?customerMessagePushDto.getImageMessage().getPic_url():null);
        //链接消息
        customerSeedMessage.setLinkUrl(customerMessagePushDto.getLinkMessage()!=null ?customerMessagePushDto.getLinkMessage().getUrl():null);
        customerSeedMessage.setLinkTitle(customerMessagePushDto.getLinkMessage()!=null ?customerMessagePushDto.getLinkMessage().getTitle():null);
        customerSeedMessage.setLinkPicurl(customerMessagePushDto.getLinkMessage()!=null ?customerMessagePushDto.getLinkMessage().getPicurl():null);
        customerSeedMessage.setLinDesc(customerMessagePushDto.getLinkMessage()!=null ?customerMessagePushDto.getLinkMessage().getDesc():null);
        //小程序消息
        customerSeedMessage.setMiniprogramTitle(customerMessagePushDto.getMiniprogramMessage()!=null ?customerMessagePushDto.getMiniprogramMessage().getTitle():null);
        customerSeedMessage.setMiniprogramMediaId(customerMessagePushDto.getMiniprogramMessage()!=null ?customerMessagePushDto.getMiniprogramMessage().getPic_media_id():null);
        customerSeedMessage.setAppid(customerMessagePushDto.getMiniprogramMessage()!=null ?customerMessagePushDto.getMiniprogramMessage().getAppid():null);
        customerSeedMessage.setPage(customerMessagePushDto.getMiniprogramMessage()!=null ?customerMessagePushDto.getMiniprogramMessage().getPage():null);
        this.save(customerSeedMessage);
    }

}
