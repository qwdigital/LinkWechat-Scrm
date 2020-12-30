package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeCustomerMessgaeResult;
import com.linkwechat.wecom.domain.vo.WeCustomerMessageResultVo;
import com.linkwechat.wecom.mapper.WeCustomerMessgaeResultMapper;
import com.linkwechat.wecom.service.IWeCustomerMessgaeResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 群发消息  微信消息发送结果表 we_customer_messgaeResult
 *
 * @author kewen
 * @date 2020-12-12
 */
@Service
public class WeCustomerMessgaeResultServiceImpl extends ServiceImpl<WeCustomerMessgaeResultMapper, WeCustomerMessgaeResult> implements IWeCustomerMessgaeResultService {

    @Autowired
    private WeCustomerMessgaeResultMapper customerMessgaeResultMapper;

    @Override
    public List<WeCustomerMessageResultVo> customerMessagePushs(Long messageId, String status) {
        return customerMessgaeResultMapper.customerMessagePushs(messageId, status);
    }

}
