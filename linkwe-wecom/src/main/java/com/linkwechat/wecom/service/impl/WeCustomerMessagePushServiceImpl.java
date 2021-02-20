package com.linkwechat.wecom.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.enums.ChatType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerMessagePushClient;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.message.*;
import com.linkwechat.wecom.domain.vo.CustomerMessagePushVo;
import com.linkwechat.wecom.mapper.WeCustomerMessageTimeTaskMapper;
import com.linkwechat.wecom.service.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 群发消息服务类
 * @author: KeWen
 * @create: 2020-12-01 20:23
 **/
@Slf4j
@Service
@SuppressWarnings("all")
public class WeCustomerMessagePushServiceImpl implements IWeCustomerMessagePushService {


    @Autowired
    private IWeCustomerMessageOriginalService weCustomerMessageOriginalService;

    @Autowired
    private WeCustomerMessagePushClient weCustomerMessagePushClient;

    @Autowired
    private IWeCustomerMessageService weCustomerMessageService;

    @Autowired
    private IWeCustomerSeedMessageService weCustomerSeedMessageService;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IWeCustomerMessgaeResultService weCustomerMessgaeResultService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WeCustomerMessageTimeTaskMapper customerMessageTimeTaskMapper;

    @Override
    @Transactional
    public void addWeCustomerMessagePush(CustomerMessagePushDto customerMessagePushDto) throws JsonProcessingException, ParseException {

        if ((null != customerMessagePushDto.getSettingTime() && !"".equals(customerMessagePushDto.getSettingTime()))
                && DateUtils.diffTime(new Date(), DateUtil.parse(customerMessagePushDto.getSettingTime(), "yyyy-MM-dd HH:mm:ss")) > 0) {
            throw new WeComException("发送时间不能小于当前时间");
        }

        List<WeCustomer> customers = Lists.newArrayList();
        List<WeGroup> groups = new ArrayList<>();
        // 0 发给客户
        if (customerMessagePushDto.getPushType().equals(WeConstans.SEND_MESSAGE_CUSTOMER)) {
            //查询客户信息列表
            customers = externalUserIds(customerMessagePushDto.getPushRange(), customerMessagePushDto.getStaffId()
                    , customerMessagePushDto.getDepartment(), customerMessagePushDto.getTag());
            if(CollectionUtils.isEmpty(customers)){
                throw new WeComException("没有外部联系人");
            }
        }

        // 1 发给客户群
        if (customerMessagePushDto.getPushType().equals(WeConstans.SEND_MESSAGE_GROUP)) {

            if (customerMessagePushDto.getStaffId() == null || customerMessagePushDto.getStaffId().equals("")) {
                throw new WeComException("请选择人员！");
            }

            //查询群组信息列表
            //通过员工id查询群列表
            WeGroup weGroup = new WeGroup();
            weGroup.setUserIds(customerMessagePushDto.getStaffId());
            groups = weGroupService.selectWeGroupList(weGroup);
            if(CollectionUtils.isEmpty(customers)){
                throw new WeComException("没有客户群！");
            }

        }

        //保存原始数据信息表
        long messageOriginalId = weCustomerMessageOriginalService.saveWeCustomerMessageOriginal(customerMessagePushDto);

        long messageId = SnowFlakeUtil.nextId();

        //保存映射信息
        int size = weCustomerMessgaeResultService.workerMappingCustomer(customerMessagePushDto, messageId,customers,groups);

        //保存微信消息
        weCustomerMessageService.saveWeCustomerMessage(messageId, messageOriginalId, customerMessagePushDto, size,customerMessagePushDto.content());

        //保存分类消息信息
        weCustomerSeedMessageService.saveSeedMessage(customerMessagePushDto, messageId);

        //发送群发消息
        //调用微信api发送消息
        if (null == customerMessagePushDto.getSettingTime() || customerMessagePushDto.getSettingTime().equals("")) {
            weCustomerMessageService.sendMessgae(customerMessagePushDto, messageId,customers,groups);
        } else {

            WeCustomerMessageTimeTask timeTask=new WeCustomerMessageTimeTask(messageId, customerMessagePushDto, customers,groups
                    ,DateUtils.getMillionSceondsBydate(customerMessagePushDto.getSettingTime()));

            customerMessageTimeTaskMapper.saveWeCustomerMessageTimeTask(timeTask);

        }

    }

    @Override
    public List<CustomerMessagePushVo> customerMessagePushs(String sender, String content, String pushType, String beginTime, String endTime) {
        return weCustomerMessageOriginalService.customerMessagePushs(sender, content, pushType, beginTime, endTime);
    }

    /**
     * 客户的外部联系人id列表，仅在chat_type为single时有效，不可与sender同时为空，最多可传入1万个客户
     *
     * @param pushRange  消息范围 0 全部客户  1 指定客户
     * @param staffId    员工id
     * @param department 部门id
     * @param tag        客户标签id列表
     * @return {@link List}s 客户的外部联系人id列表
     */
    public List<WeCustomer> externalUserIds(String pushRange, String staffId, String department, String tag) {
        if (pushRange.equals(WeConstans.SEND_MESSAGE_CUSTOMER_ALL)) {
            //从redis中读取数据
            return redisCache.getCacheList(WeConstans.WECUSTOMERS_KEY);
        } else {
            //按条件查询客户
            //通过部门id查询所有的员工
            //通过员工列表查询该员工的外部联系人列表
            WeCustomer weCustomer = new WeCustomer();
            weCustomer.setUserIds(staffId);
            weCustomer.setTagIds(tag);
            weCustomer.setDepartmentIds(department);
            return weCustomerService.selectWeCustomerList(weCustomer);
        }
    }

}
