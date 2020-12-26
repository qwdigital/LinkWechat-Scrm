package com.linkwechat.wecom.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.ChatType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.wecom.client.WeCustomerMessagePushClient;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.message.*;
import com.linkwechat.wecom.domain.vo.CustomerMessagePushVo;
import com.linkwechat.wecom.service.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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
    private IWeCustomerImageMessageService weCustomerImageMessageService;

    @Autowired
    private IWeCustomerLinkMessageService weCustomerLinkMessageService;

    @Autowired
    private IWeCustomerMessageService weCustomerMessageService;

    @Autowired
    private IWeCustomerMiniprogramMessageService weCustomerMiniprogramMessageService;

    @Autowired
    private IWeCustomerTextMessagService weCustomerTextMessagService;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public void addWeCustomerMessagePush(CustomerMessagePushDto customerMessagePushDto) throws JsonProcessingException {
        //立即发送
        if (null == customerMessagePushDto.getSettingTime() || customerMessagePushDto.getSettingTime().equals("")) {

            customerMessagePushDto.setMessageType("1");
            sendMessgae(customerMessagePushDto);

        } else {

            //发送时间不能小于当前时间 
            //定时发送消息(异步执行)
            //保存消息体到数据库，增加一个发送状态(0 已发送 1 未发送)
            //定义一个任务执行队列
            //把任务放入执行队列
            //任务执行 sendMessgae(WeMessagePush weMessagePush) 方法
            if (DateUtils.diffTime(new Date(), DateUtil.parse(customerMessagePushDto.getSettingTime(), "yyyy-MM-dd HH:mm:ss")) > 0) {
                throw new WeComException("发送时间不能小于当前时间");
            }

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @SneakyThrows
                @Override
                public void run() {
                    sendMessgae(customerMessagePushDto);
                }
            }, DateUtil.parse(customerMessagePushDto.getSettingTime(), "yyyy-MM-dd HH:mm:ss"));

        }

    }

    @Override
    public List<CustomerMessagePushVo> customerMessagePushs(String sender, String content, String pushType, String beginTime, String endTime) {
        return weCustomerMessageOriginalService.customerMessagePushs(sender, content, pushType, beginTime, endTime);
    }

    /**
     * 发送消息
     *
     * @param customerMessagePushDto 消息信息
     * @throws JsonProcessingException
     */
    public void sendMessgae(CustomerMessagePushDto customerMessagePushDto) throws JsonProcessingException {

        //原始数据信息表 WeCustomerMessageOriginal 主键id
        long messageOriginalId = SnowFlakeUtil.nextId();
        WeCustomerMessageOriginal original = new WeCustomerMessageOriginal();
        original.setMessageOriginalId(messageOriginalId);
        original.setStaffId(customerMessagePushDto.getStaffId());
        original.setDepartment(customerMessagePushDto.getDepartment());
        original.setPushType(customerMessagePushDto.getPushType());
        original.setMessageType(customerMessagePushDto.getMessageType());
        original.setPushRange(customerMessagePushDto.getPushRange());
        original.setTag(customerMessagePushDto.getTag());
        original.setDelFlag(0);
        weCustomerMessageOriginalService.save(original);

        List<String> msgIds = Lists.newArrayList();
        List<String> senders = Lists.newArrayList();
        List<String> externalUserIds = Lists.newArrayList();
        StringBuffer sendInfo = new StringBuffer();
        //预计发送的人员和群的个数
        int size = 0;

        //发送群发消息
        //发送类类型: 给单个客户发，群发
        //发给客户
        if (customerMessagePushDto.getPushType().equals("0")) {
            WeCustomerMessagePushDto messagePushDto = new WeCustomerMessagePushDto();
            messagePushDto.setChat_type(ChatType.of(customerMessagePushDto.getPushType()).getName());
            externalUserIds = externalUserIds(customerMessagePushDto.getPushRange(), customerMessagePushDto.getStaffId()
                    , customerMessagePushDto.getDepartment(), customerMessagePushDto.getTag());
            if (CollectionUtils.isNotEmpty(externalUserIds)) {
                size = externalUserIds.size();
            }
            messagePushDto.setExternal_userid(externalUserIds);

            seedMessage(messagePushDto, customerMessagePushDto);
            SendMessageResultDto sendMessageResultDto = weCustomerMessagePushClient.sendCustomerMessageToUser(messagePushDto);
            if (WeConstans.WE_SUCCESS_CODE.equals(sendMessageResultDto.getErrcode())) {
                msgIds.add(sendMessageResultDto.getMsgid());
            }

        }

        //发给客户群
        if (customerMessagePushDto.getPushType().equals("1")) {
            if (customerMessagePushDto.getStaffId() == null || customerMessagePushDto.getStaffId().equals("")) {
                throw new WeComException("参数异常！");
            }
            //通过员工id查询群列表
            WeGroup weGroup = new WeGroup();
            weGroup.setUserIds(customerMessagePushDto.getStaffId());
            List<WeGroup> groups = weGroupService.selectWeGroupList(weGroup);
            if (CollectionUtils.isNotEmpty(groups)) {
                size = groups.size();
                List<String> owners = groups.stream().map(WeGroup::getOwner).collect(Collectors.toList());
                for (String owner : owners) {
                    WeCustomerMessagePushDto messagePushDto = new WeCustomerMessagePushDto();
                    messagePushDto.setChat_type(ChatType.of(customerMessagePushDto.getPushType()).getType());
                    //客户群的员工id
                    messagePushDto.setSender(owner);
                    seedMessage(messagePushDto, customerMessagePushDto);
                    SendMessageResultDto sendMessageResultDto = weCustomerMessagePushClient.sendCustomerMessageToUser(messagePushDto);
                    if (WeConstans.WE_SUCCESS_CODE.equals(sendMessageResultDto.getErrcode())) {
                        //发送的msgId
                        msgIds.add(sendMessageResultDto.getMsgid());
                        senders.add(owner);
                    }
                }
            }
        }

        sendInfo(customerMessagePushDto, sendInfo, size);

        //微信群发消息表 WeCustomerMessage
        long messageId = SnowFlakeUtil.nextId();
        WeCustomerMessage customerMessage = new WeCustomerMessage();
        customerMessage.setOriginalId(messageOriginalId);
        customerMessage.setMessageId(messageId);
        customerMessage.setChatType(customerMessagePushDto.getPushType());
        customerMessage.setExternalUserid(objectMapper.writeValueAsString(externalUserIds));
        customerMessage.setSender(objectMapper.writeValueAsString(senders));
        customerMessage.setCheckStatus("0");
        customerMessage.setMsgid(objectMapper.writeValueAsString(msgIds));
        customerMessage.setDelFlag(0);
        customerMessage.setContent(content(customerMessagePushDto));
        customerMessage.setSendInfo(sendInfo.toString());
        weCustomerMessageService.save(customerMessage);

        //保存分类消息信息
        saveSeedMessage(customerMessagePushDto, messageId);

    }

    private void sendInfo(CustomerMessagePushDto customerMessagePushDto, StringBuffer sendInfo, int size) {
        if (null != customerMessagePushDto.getSettingTime() && !customerMessagePushDto.getSettingTime().equals("")) {
            sendInfo.append("定时任务");
            sendInfo.append("发送时间:");
            sendInfo.append(customerMessagePushDto.getSettingTime());
        } else {
            if (customerMessagePushDto.getPushType().equals("0")) {
                //预计发送3190人，已成功发送0人
                sendInfo.append("预计发送");
                sendInfo.append(size);
                sendInfo.append("人,");
                sendInfo.append("已成功发送0人");
            }
            if (customerMessagePushDto.getPushType().equals("1")) {
                //预计发送2个群，已成功发送0个群
                sendInfo.append("预计发送");
                sendInfo.append(size);
                sendInfo.append("个群，");
                sendInfo.append("已成功发送0个群");
            }
        }
    }


    /**
     * 消息内容
     *
     * @param customerMessagePushDto
     * @return
     */
    private String content(CustomerMessagePushDto customerMessagePushDto) {
        if (customerMessagePushDto != null) {
            // 消息类型 0 文本消息  1 图片消息 2 链接消息   3 小程序消息
            if (customerMessagePushDto.getMessageType().equals("0")) {
                return customerMessagePushDto.getTextMessage().getContent();
            }
            if (customerMessagePushDto.getMessageType().equals("1")) {

                return customerMessagePushDto.getImageMessage().getPic_url();
            }

            if (customerMessagePushDto.getMessageType().equals("2")) {

                return customerMessagePushDto.getLinkMessage().getTitle() + ":" + customerMessagePushDto.getLinkMessage().getPicurl()
                        + ":" + customerMessagePushDto.getLinkMessage().getUrl() + "" + customerMessagePushDto.getLinkMessage().getDesc();
            }

            if (customerMessagePushDto.getMessageType().equals("3")) {

                return customerMessagePushDto.getMiniprogramMessage().getTitle() + ":" + customerMessagePushDto.getMiniprogramMessage().getPage();
            }
        }
        return null;
    }

    /**
     * 子消息体
     *
     * @param weCustomerMessagePushDto 群发消息体
     * @param customerMessagePushDto   群发消息
     */
    public String seedMessage(WeCustomerMessagePushDto weCustomerMessagePushDto, CustomerMessagePushDto customerMessagePushDto) {
        String content = null;
        // 消息类型 0 文本消息  1 图片消息 2 链接消息   3 小程序消息
        if (customerMessagePushDto.getMessageType().equals("0")) {
            weCustomerMessagePushDto.setImage(null);
            weCustomerMessagePushDto.setLink(null);
            weCustomerMessagePushDto.setMiniprogram(null);
            weCustomerMessagePushDto.setText(customerMessagePushDto.getTextMessage());
            content = customerMessagePushDto.getTextMessage().getContent();
        }
        if (customerMessagePushDto.getMessageType().equals("1")) {
            weCustomerMessagePushDto.setImage(customerMessagePushDto.getImageMessage());
            weCustomerMessagePushDto.setLink(null);
            weCustomerMessagePushDto.setMiniprogram(null);
            weCustomerMessagePushDto.setText(null);
            content = customerMessagePushDto.getImageMessage().getPic_url();
        }

        if (customerMessagePushDto.getMessageType().equals("2")) {
            weCustomerMessagePushDto.setImage(null);
            weCustomerMessagePushDto.setLink(customerMessagePushDto.getLinkMessage());
            weCustomerMessagePushDto.setMiniprogram(null);
            weCustomerMessagePushDto.setText(null);
            content = customerMessagePushDto.getLinkMessage().getTitle() + ":" + customerMessagePushDto.getLinkMessage().getPicurl()
                    + ":" + customerMessagePushDto.getLinkMessage().getUrl() + "" + customerMessagePushDto.getLinkMessage().getDesc();
        }

        if (customerMessagePushDto.getMessageType().equals("3")) {
            weCustomerMessagePushDto.setImage(null);
            weCustomerMessagePushDto.setLink(null);
            weCustomerMessagePushDto.setMiniprogram(customerMessagePushDto.getMiniprogramMessage());
            weCustomerMessagePushDto.setText(null);
            content = customerMessagePushDto.getMiniprogramMessage().getTitle() + ":" + customerMessagePushDto.getMiniprogramMessage().getPage();
        }
        return content;
    }

    /**
     * 各分类消息表
     *
     * @param customerMessagePushDto 群发消息
     * @param messageId              消息id
     */
    public void saveSeedMessage(CustomerMessagePushDto customerMessagePushDto, long messageId) {
        String messageType = customerMessagePushDto.getMessageType();
        switch (messageType) {
            case "0":
                //文本消息
                WeCustomerTextMessag textMessage = new WeCustomerTextMessag();
                textMessage.setTextMessageId(SnowFlakeUtil.nextId());
                textMessage.setMessageId(messageId);
                TextMessageDto textMessageDto = customerMessagePushDto.getTextMessage();
                textMessage.setContent(textMessageDto.getContent());
                textMessage.setDelFlag(0);
                weCustomerTextMessagService.save(textMessage);
                break;
            case "1":
                //图片消息
                WeCustomerImageMessage imageMessage = new WeCustomerImageMessage();
                imageMessage.setImageMessageId(SnowFlakeUtil.nextId());
                imageMessage.setMessageId(messageId);
                ImageMessageDto imageMessageDto = customerMessagePushDto.getImageMessage();
                imageMessage.setMediaId(imageMessageDto.getMedia_id());
                imageMessage.setPicUrl(imageMessageDto.getPic_url());
                imageMessage.setDelFlag(0);
                weCustomerImageMessageService.save(imageMessage);
                break;
            case "2":
                //链接消息
                WeCustomerLinkMessage linkMessage = new WeCustomerLinkMessage();
                linkMessage.setLinkMessageId(SnowFlakeUtil.nextId());
                linkMessage.setMessageId(messageId);
                LinkMessageDto linkMessageDto = customerMessagePushDto.getLinkMessage();
                linkMessage.setUrl(linkMessageDto.getUrl());
                linkMessage.setPicurl(linkMessageDto.getPicurl());
                linkMessage.setTitle(linkMessageDto.getTitle());
                linkMessage.setDesc(linkMessageDto.getDesc());
                linkMessage.setDelFlag(0);
                weCustomerLinkMessageService.save(linkMessage);
                break;
            case "3":
                //小程序消息
                WeCustomerMiniprogramMessage miniprogramMessage = new WeCustomerMiniprogramMessage();
                miniprogramMessage.setMiniprogramMessageId(SnowFlakeUtil.nextId());
                miniprogramMessage.setMessageId(messageId);
                MiniprogramMessageDto miniprogramMessageDto = customerMessagePushDto.getMiniprogramMessage();
                miniprogramMessage.setAppid(miniprogramMessageDto.getAppid());
                miniprogramMessage.setPage(miniprogramMessageDto.getPage());
                miniprogramMessage.setPicMediaId(miniprogramMessageDto.getPic_media_id());
                miniprogramMessage.setTitle(miniprogramMessageDto.getTitle());
                miniprogramMessage.setDelFlag(0);
                weCustomerMiniprogramMessageService.save(miniprogramMessage);
                break;
        }
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
    public List<String> externalUserIds(String pushRange, String staffId, String department, String tag) {
        List<String> customers = Lists.newArrayList();
        if (pushRange.equals("0")) {
            //查询系统所有客户
            List<WeCustomer> weCustomers = weCustomerService.selectWeCustomerList(null);
            if (CollectionUtils.isNotEmpty(weCustomers)) {
                customers = weCustomers.stream().map(WeCustomer::getExternalUserid).distinct().collect(Collectors.toList());
            }
        } else {
            //按条件查询客户
            //通过部门id查询所有的员工
            //通过员工列表查询该员工的外部联系人列表
            WeCustomer weCustomer = new WeCustomer();
            weCustomer.setUserIds(staffId);
            weCustomer.setTagIds(tag);
            weCustomer.setDepartmentIds(department);
            List<WeCustomer> weCustomers = weCustomerService.selectWeCustomerList(weCustomer);
            if (CollectionUtils.isNotEmpty(weCustomers)) {
                customers = weCustomers.stream().map(WeCustomer::getExternalUserid).distinct().collect(Collectors.toList());
            }

        }
        return customers;
    }


}
