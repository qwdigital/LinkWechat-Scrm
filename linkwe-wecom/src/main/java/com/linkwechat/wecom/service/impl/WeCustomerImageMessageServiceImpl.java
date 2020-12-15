package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeCustomerImageMessage;
import com.linkwechat.wecom.mapper.WeCustomerImageMessageMapper;
import com.linkwechat.wecom.service.IWeCustomerImageMessageService;
import org.springframework.stereotype.Service;

/**
 * 群发消息  图片消息表 we_customer_imageMessage
 *
 * @author kewen
 * @date 2020-12-12
 */
@Service
public class WeCustomerImageMessageServiceImpl extends ServiceImpl<WeCustomerImageMessageMapper, WeCustomerImageMessage> implements IWeCustomerImageMessageService {
}
