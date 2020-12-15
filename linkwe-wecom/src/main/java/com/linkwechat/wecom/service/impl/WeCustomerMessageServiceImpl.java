package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeCustomerMessage;
import com.linkwechat.wecom.mapper.WeCustomerMessageMapper;
import com.linkwechat.wecom.service.IWeCustomerMessageService;
import org.springframework.stereotype.Service;

/**
 * 群发消息  微信消息表service接口
 *
 * @author kewen
 * @date 2020-12-12
 */
@Service
public class WeCustomerMessageServiceImpl extends ServiceImpl<WeCustomerMessageMapper, WeCustomerMessage>  implements IWeCustomerMessageService {
}
