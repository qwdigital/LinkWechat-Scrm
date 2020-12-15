package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeCustomerMiniprogramMessage;
import com.linkwechat.wecom.mapper.WeCustomerMiniprogramMessageMapper;
import com.linkwechat.wecom.service.IWeCustomerMiniprogramMessageService;
import org.springframework.stereotype.Service;

/**
 * 群发消息  小程序消息表 we_customer_miniprogramMessage
 *
 * @author kewen
 * @date 2020-12-12
 */
@Service
public class WeCustomerMiniprogramMessageServiceImpl extends ServiceImpl<WeCustomerMiniprogramMessageMapper, WeCustomerMiniprogramMessage> implements IWeCustomerMiniprogramMessageService {
}
