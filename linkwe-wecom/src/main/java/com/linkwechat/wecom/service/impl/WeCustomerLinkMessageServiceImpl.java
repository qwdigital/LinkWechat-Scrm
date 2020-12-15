package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeCustomerLinkMessage;
import com.linkwechat.wecom.mapper.WeCustomerLinkMessageMapper;
import com.linkwechat.wecom.service.IWeCustomerLinkMessageService;
import org.springframework.stereotype.Service;
/**
 * 群发消息  链接消息表 we_customer_linkMessage
 *
 * @author kewen
 * @date 2020-12-13
 */
@Service
public class WeCustomerLinkMessageServiceImpl extends ServiceImpl<WeCustomerLinkMessageMapper, WeCustomerLinkMessage> implements IWeCustomerLinkMessageService {
}
