package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeCustomerList;
import com.linkwechat.wecom.domain.WeCustomerMessgaeResult;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.dto.message.CustomerMessagePushDto;
import com.linkwechat.wecom.domain.vo.WeCustomerMessageResultVo;
import com.linkwechat.wecom.mapper.WeCustomerMessgaeResultMapper;
import com.linkwechat.wecom.service.IWeCustomerMessgaeResultService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public int workerMappingCustomer(CustomerMessagePushDto customerMessagePushDto, long messageId, List<WeCustomerList> customers, List<WeGroup> groups) {

        int size = 0;
        // 0 发给客户
        if (customerMessagePushDto.getPushType().equals(WeConstans.SEND_MESSAGE_CUSTOMER)) {

            if (CollectionUtils.isNotEmpty(customers)) {

                List<WeCustomerMessgaeResult> weCustomerMessgaeResults=Lists.newArrayList();

                size = customers.size();
                customers.forEach(customer -> {
                    //微信消息发送结果、保存员工客户关系映射关系数据
                    WeCustomerMessgaeResult customerMessgaeResult = new WeCustomerMessgaeResult();
                    customerMessgaeResult.setMessgaeResultId(SnowFlakeUtil.nextId());
                    customerMessgaeResult.setMessageId(messageId);
                    //这个是实际发送时间
                    customerMessgaeResult.setSendTime(null);
                    customerMessgaeResult.setSendType(customerMessgaeResult.getSettingTime() == null ? customerMessagePushDto.getPushType() : "2");
                    customerMessgaeResult.setExternalUserid(customer.getExternalUserid());
                    customerMessgaeResult.setExternalName(customer.getName());
                    customerMessgaeResult.setUserid(customer.getFirstUserId());
                    customerMessgaeResult.setUserName(customer.getUserName());
                    customerMessgaeResult.setStatus("0");
                    customerMessgaeResult.setSettingTime(customerMessagePushDto.getSettingTime());
                    customerMessgaeResult.setDelFlag(0);
                    weCustomerMessgaeResults.add(customerMessgaeResult);
                });
                customerMessgaeResultMapper.batchInsert(weCustomerMessgaeResults);
            }
        }

        // 1 发给客户群
        if (customerMessagePushDto.getPushType().equals(WeConstans.SEND_MESSAGE_GROUP)) {

            if (CollectionUtils.isNotEmpty(groups)) {
                List<WeCustomerMessgaeResult> weCustomerMessgaeResults=Lists.newArrayList();
                size = groups.size();
                groups.forEach(group -> {
                    //微信消息发送结果、保存员工客户关系映射关系数据
                    WeCustomerMessgaeResult customerMessgaeResult = new WeCustomerMessgaeResult();
                    customerMessgaeResult.setMessgaeResultId(SnowFlakeUtil.nextId());
                    customerMessgaeResult.setMessageId(messageId);
                    customerMessgaeResult.setSendTime(customerMessagePushDto.getSettingTime());
                    customerMessgaeResult.setSendType(customerMessgaeResult.getSettingTime() == null ? customerMessagePushDto.getPushType() : "2");
                    customerMessgaeResult.setSettingTime(customerMessagePushDto.getSettingTime());
                    customerMessgaeResult.setChatId(group.getChatId());
                    customerMessgaeResult.setChatName(group.getGroupName());
                    customerMessgaeResult.setStatus("0");
                    //群管理员id
                    customerMessgaeResult.setUserid(group.getGroupLeader());
                    //群管理员名称
                    customerMessgaeResult.setUserName(group.getGroupLeaderName());
                    customerMessgaeResult.setDelFlag(0);
                    weCustomerMessgaeResults.add(customerMessgaeResult);
                });
                customerMessgaeResultMapper.batchInsert(weCustomerMessgaeResults);
            }

        }
        return size;
    }

}
