package com.linkwechat.wecom.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.ChatType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
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

    @Override
    @Transactional
    public void addWeCustomerMessagePush(CustomerMessagePushDto customerMessagePushDto) throws JsonProcessingException {

        if ((null != customerMessagePushDto.getSettingTime() && !"".equals(customerMessagePushDto.getSettingTime())) && DateUtils.diffTime(new Date(), DateUtil.parse(customerMessagePushDto.getSettingTime(), "yyyy-MM-dd HH:mm:ss")) > 0) {
            throw new WeComException("发送时间不能小于当前时间");
        }

        //查询员id
        List<String> senders = Lists.newArrayList();

        StringBuffer sendInfo = new StringBuffer();

        //保存原始数据信息表
        long messageOriginalId = saveWeCustomerMessageOriginal(customerMessagePushDto);

        long messageId = SnowFlakeUtil.nextId();

        //保存映射信息
        int size = workerMappingCustomer(customerMessagePushDto, messageId);

        sendInfo(customerMessagePushDto, sendInfo, size);

        //保存微信消息
        saveWeCustomerMessage(messageId, messageOriginalId, customerMessagePushDto, sendInfo);

        //保存分类消息信息
        weCustomerSeedMessageService.saveSeedMessage(customerMessagePushDto, messageId);

        //发送群发消息
        //调用微信api发送消息
        if (null == customerMessagePushDto.getSettingTime() || customerMessagePushDto.getSettingTime().equals("")) {
            sendMessgae(customerMessagePushDto, messageId);
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
                    sendMessgae(customerMessagePushDto, messageId);

                }
            }, DateUtil.parse(customerMessagePushDto.getSettingTime(), "yyyy-MM-dd HH:mm:ss"));

        }

    }

    //保存映射信息
    private int workerMappingCustomer(CustomerMessagePushDto customerMessagePushDto, long messageId) {
        List<WeCustomer> customers = Lists.newArrayList();
        List<WeGroup> groups = new ArrayList<>();
        int size = 0;
        // 0 发给客户
        if (customerMessagePushDto.getPushType().equals("0")) {

            customers = externalUserIds(customerMessagePushDto.getPushRange(), customerMessagePushDto.getStaffId()
                    , customerMessagePushDto.getDepartment(), customerMessagePushDto.getTag());
            if (CollectionUtils.isNotEmpty(customers)) {
                size = customers.size();
                customers.forEach(customer -> {
                    //微信消息发送结果、保存员工客户关系映射关系数据
                    WeCustomerMessgaeResult customerMessgaeResult = new WeCustomerMessgaeResult();
                    customerMessgaeResult.setMessgaeResultId(SnowFlakeUtil.nextId());
                    customerMessgaeResult.setMessageId(messageId);
                    customerMessgaeResult.setSendTime(customerMessagePushDto.getSettingTime());
                    customerMessgaeResult.setSendType(customerMessgaeResult.getSettingTime() == null ? customerMessagePushDto.getPushType() : "2");
                    customerMessgaeResult.setExternalUserid(customer.getExternalUserid());
                    customerMessgaeResult.setExternalName(customer.getName());
                    customerMessgaeResult.setUserid(customer.getUserId());
                    customerMessgaeResult.setUserName(customer.getUserName());
                    customerMessgaeResult.setStatus("0");
                    customerMessgaeResult.setSettingTime(customerMessagePushDto.getSettingTime());
                    customerMessgaeResult.setDelFlag(0);
                    weCustomerMessgaeResultService.save(customerMessgaeResult);
                });
            }
        }

        // 1 发给客户群
        if (customerMessagePushDto.getPushType().equals("1")) {
            //通过员工id查询群列表
            WeGroup weGroup = new WeGroup();
            weGroup.setUserIds(customerMessagePushDto.getStaffId());
            groups = weGroupService.selectWeGroupList(weGroup);
            if (CollectionUtils.isNotEmpty(groups)) {
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
                    weCustomerMessgaeResultService.save(customerMessgaeResult);
                });
            }

        }
        return size;
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

    //保存微信消息  WeCustomerMessage
    public void saveWeCustomerMessage(long messageId, long messageOriginalId, CustomerMessagePushDto customerMessagePushDto, StringBuffer sendInfo) {
        //保存微信消息
        //微信群发消息表 WeCustomerMessage
        WeCustomerMessage customerMessage = new WeCustomerMessage();
        customerMessage.setOriginalId(messageOriginalId);
        customerMessage.setMessageId(messageId);
        customerMessage.setChatType(customerMessagePushDto.getPushType());
        // customerMessage.setExternalUserid(objectMapper.writeValueAsString(customers));
        // customerMessage.setSender(objectMapper.writeValueAsString(senders));
        customerMessage.setCheckStatus("0");
        customerMessage.setDelFlag(0);
        customerMessage.setContent(content(customerMessagePushDto));
        customerMessage.setSendInfo(sendInfo.toString());
        customerMessage.setCreateBy(SecurityUtils.getUsername());
        customerMessage.setMsgid("");
        weCustomerMessageService.save(customerMessage);
    }

    //保存原始数据信息表 WeCustomerMessageOriginal 主键id
    public long saveWeCustomerMessageOriginal(CustomerMessagePushDto customerMessagePushDto) {
        //保存原始数据信息表 WeCustomerMessageOriginal 主键id
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
        return messageOriginalId;
    }


    @Override
    public List<CustomerMessagePushVo> customerMessagePushs(String sender, String content, String pushType, String beginTime, String endTime) {
        return weCustomerMessageOriginalService.customerMessagePushs(sender, content, pushType, beginTime, endTime);
    }

    /**
     * 发送消息
     *
     * @param customerMessagePushDto 消息信息
     * @param messageId
     * @throws JsonProcessingException
     */
    public void sendMessgae(CustomerMessagePushDto customerMessagePushDto, long messageId) throws JsonProcessingException {
        List<String> msgid = new ArrayList<>();
        //发送群发消息
        //发送类类型: 给单个客户发，群发
        //发给客户
        if (customerMessagePushDto.getPushType().equals("0")) {
            WeCustomerMessagePushDto messagePushDto = new WeCustomerMessagePushDto();
            messagePushDto.setChat_type(ChatType.of(customerMessagePushDto.getPushType()).getName());
            List<String> externalUserIds = externalUserIds(customerMessagePushDto.getPushRange(), customerMessagePushDto.getStaffId()
                    , customerMessagePushDto.getDepartment(), customerMessagePushDto.getTag()).stream().map(WeCustomer::getExternalUserid).collect(Collectors.toList());
            messagePushDto.setExternal_userid(externalUserIds);
            seedMessage(messagePushDto, customerMessagePushDto);
            SendMessageResultDto sendMessageResultDto = weCustomerMessagePushClient.sendCustomerMessageToUser(messagePushDto);
            if (WeConstans.WE_SUCCESS_CODE.equals(sendMessageResultDto.getErrcode())) {
                msgid.add(sendMessageResultDto.getMsgid());
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
                        msgid.add(sendMessageResultDto.getMsgid());
                    }
                }
            }
        }

        updateMsgId(messageId, msgid);

    }


    public void updateMsgId(long messageId, List<String> msgIds) throws JsonProcessingException {
        //通过messageId更新msgIds
        WeCustomerMessage weCustomerMessage = new WeCustomerMessage();
        weCustomerMessage.setMessageId(messageId);
        weCustomerMessage.setMsgid(objectMapper.writeValueAsString(msgIds));
        weCustomerMessageService.updateWeCustomerMessageMsgId(weCustomerMessage);
    }

    /**
     * 子消息体
     *
     * @param weCustomerMessagePushDto 群发消息体
     * @param customerMessagePushDto   群发消息
     */
    public void seedMessage(WeCustomerMessagePushDto weCustomerMessagePushDto, CustomerMessagePushDto customerMessagePushDto) {

        // 消息类型 0 文本消息  1 图片消息 2 链接消息   3 小程序消息
        if (customerMessagePushDto.getMessageType().equals("0")) {
            weCustomerMessagePushDto.setImage(null);
            weCustomerMessagePushDto.setLink(null);
            weCustomerMessagePushDto.setMiniprogram(null);
            weCustomerMessagePushDto.setText(customerMessagePushDto.getTextMessage());

        }
        if (customerMessagePushDto.getMessageType().equals("1")) {
            weCustomerMessagePushDto.setImage(customerMessagePushDto.getImageMessage());
            weCustomerMessagePushDto.setLink(null);
            weCustomerMessagePushDto.setMiniprogram(null);
            weCustomerMessagePushDto.setText(null);

        }
        if (customerMessagePushDto.getMessageType().equals("2")) {
            weCustomerMessagePushDto.setImage(null);
            weCustomerMessagePushDto.setLink(customerMessagePushDto.getLinkMessage());
            weCustomerMessagePushDto.setMiniprogram(null);
            weCustomerMessagePushDto.setText(null);

        }

        if (customerMessagePushDto.getMessageType().equals("3")) {
            weCustomerMessagePushDto.setImage(null);
            weCustomerMessagePushDto.setLink(null);
            weCustomerMessagePushDto.setMiniprogram(customerMessagePushDto.getMiniprogramMessage());
            weCustomerMessagePushDto.setText(null);

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
    public List<WeCustomer> externalUserIds(String pushRange, String staffId, String department, String tag) {
        if (pushRange.equals("0")) {
            //查询系统所有客户
            return weCustomerService.selectWeCustomerList(null);
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
