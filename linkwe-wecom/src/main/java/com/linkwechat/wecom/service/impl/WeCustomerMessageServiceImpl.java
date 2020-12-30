package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.wecom.domain.WeCustomerMessage;
import com.linkwechat.wecom.mapper.WeCustomerMessageMapper;
import com.linkwechat.wecom.service.IWeCustomerMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 群发消息  微信消息表service接口
 *
 * @author kewen
 * @date 2020-12-12
 */
@Service
public class WeCustomerMessageServiceImpl extends ServiceImpl<WeCustomerMessageMapper, WeCustomerMessage>  implements IWeCustomerMessageService {

    @Autowired
    private WeCustomerMessageMapper weCustomerMessageMapper;
    @Override
    public int updateWeCustomerMessageMsgId(WeCustomerMessage customerMessage) {
        return weCustomerMessageMapper.updateWeCustomerMessageMsgIdById(customerMessage);
    }

}
