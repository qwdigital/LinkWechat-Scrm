package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeCustomerTextMessag;
import com.linkwechat.wecom.mapper.WeCustomerTextMessagMapper;
import com.linkwechat.wecom.service.IWeCustomerTextMessagService;
import org.springframework.stereotype.Service;

/**
 * 群发消息  文本消息表 we_customer_textMessage
 *
 * @author kewen
 * @date 2020-12-12
 */
@Service
public class WeCustomerTextMessagServiceImpl extends ServiceImpl<WeCustomerTextMessagMapper, WeCustomerTextMessag> implements IWeCustomerTextMessagService {
}
