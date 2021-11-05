package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.ChatType;
import com.linkwechat.common.enums.GroupMessageType;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerMessagePushClient;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeCustomerList;
import com.linkwechat.wecom.domain.WeCustomerMessage;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.dto.message.*;
import com.linkwechat.wecom.mapper.WeCustomerMessageMapper;
import com.linkwechat.wecom.service.IWeCustomerMessageService;
import com.linkwechat.wecom.service.IWeMaterialService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 群发消息  微信消息表service接口
 *
 * @author kewen
 * @date 2020-12-12
 */
@SuppressWarnings("all")
@Service
public class WeCustomerMessageServiceImpl extends ServiceImpl<WeCustomerMessageMapper, WeCustomerMessage>  implements IWeCustomerMessageService {

    @Autowired
    private WeCustomerMessageMapper weCustomerMessageMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WeCustomerMessagePushClient weCustomerMessagePushClient;

    @Autowired
    private IWeMaterialService weMaterialService;


    @Override
    public int updateWeCustomerMessageActualSend(Long messageId, Integer actualSend) {
        return weCustomerMessageMapper.updateWeCustomerMessageActualSend(messageId, actualSend);
    }

    @Override
    public void saveWeCustomerMessage(long messageId, long messageOriginalId, CustomerMessagePushDto customerMessagePushDto, int size,String content) {
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
        customerMessage.setContent(content);
        customerMessage.setCreateBy(SecurityUtils.getUsername());
        customerMessage.setMsgid("");
        if(StringUtils.isNotEmpty(customerMessagePushDto.getSettingTime())){
            customerMessage.setSettingTime(customerMessagePushDto.getSettingTime());
            customerMessage.setTimedTask(1);
        }else {
            customerMessage.setTimedTask(0);
        }
        customerMessage.setExpectSend(size);
        customerMessage.setActualSend(0);
        this.save(customerMessage);
    }

    @Override
    public void updateMsgId(long messageId, List<String> msgIds) throws JsonProcessingException {
        //通过messageId更新msgIds
        WeCustomerMessage weCustomerMessage = new WeCustomerMessage();
        weCustomerMessage.setMessageId(messageId);
//        String result = msgIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        weCustomerMessage.setMsgid(msgIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
//        weCustomerMessage.setMsgid(objectMapper.writeValueAsString(msgIds));
        weCustomerMessageMapper.updateWeCustomerMessageMsgIdById(weCustomerMessage);
    }

    @Override
    public void sendMessgae(CustomerMessagePushDto customerMessagePushDto, long messageId, List<WeCustomerList> customers, List<WeGroup> groups) throws JsonProcessingException {
        List<String> msgid = new ArrayList<>();

        //发给客户
        if(CollectionUtils.isNotEmpty(customers)){

            customers.stream().filter(customer -> StringUtils.isNotEmpty(customer.getFirstUserId()))
                    .collect(Collectors.groupingBy(WeCustomerList::getFirstUserId)).forEach((k,v)->{

                customerMessagePushDto.setStaffId(k);
                //发送群发消息
                //发送类类型: 给单个客户发，群发
                if (customerMessagePushDto.getPushType().equals(WeConstans.SEND_MESSAGE_CUSTOMER)) {

                    WeCustomerMessagePushDto messagePushDto = new WeCustomerMessagePushDto();

                    messagePushDto.setChat_type(ChatType.of(customerMessagePushDto.getPushType()).getName());
                    List<String> externalUserIds = v.stream().map(WeCustomerList::getExternalUserid).collect(Collectors.toList());

                    messagePushDto.setExternal_userid(externalUserIds);
                    messagePushDto.setSender(customerMessagePushDto.getStaffId());
                    childMessage(messagePushDto, customerMessagePushDto);

                    SendMessageResultDto sendMessageResultDto = weCustomerMessagePushClient.sendCustomerMessageToUser(messagePushDto);
                    if (WeConstans.WE_SUCCESS_CODE.equals(sendMessageResultDto.getErrcode())) {
                        msgid.add(sendMessageResultDto.getMsgid());
                    }
                }
            });
        }




        //发给客户群
        if(CollectionUtil.isNotEmpty(groups)){
            if (customerMessagePushDto.getPushType().equals(WeConstans.SEND_MESSAGE_GROUP)) {
                if (CollectionUtils.isNotEmpty(groups)) {
                    Set<String> owners = groups.stream().map(WeGroup::getOwner).collect(Collectors.toSet());
                    for (String owner : owners) {
                        WeCustomerMessagePushDto messagePushDto = new WeCustomerMessagePushDto();
                        messagePushDto.setChat_type(ChatType.of(customerMessagePushDto.getPushType()).getName());
                        //客户群的员工id
                        messagePushDto.setSender(owner);
                        childMessage(messagePushDto, customerMessagePushDto);
                        SendMessageResultDto sendMessageResultDto = weCustomerMessagePushClient.sendCustomerMessageToUser(messagePushDto);
                        if (WeConstans.WE_SUCCESS_CODE.equals(sendMessageResultDto.getErrcode())) {
                            //发送的msgId
                            msgid.add(sendMessageResultDto.getMsgid());
                        }
                    }
                }

            }
        }



        this.updateMsgId(messageId, msgid);

    }


    /**
     * 子消息体
     *
     * @param weCustomerMessagePushDto 群发消息体
     * @param customerMessagePushDto   群发消息
     */
    public void childMessage(WeCustomerMessagePushDto weCustomerMessagePushDto, CustomerMessagePushDto customerMessagePushDto) {
        List list = new ArrayList();
        // 消息类型 0 文本消息  1 图片消息 2 链接消息   3 小程序消息
        if (customerMessagePushDto.getMessageType().equals(GroupMessageType.TEXT.getType())) {
            weCustomerMessagePushDto.setText(customerMessagePushDto.getTextMessage());
        }

        if (customerMessagePushDto.getMessageType().equals(GroupMessageType.IMAGE.getType())) {
            ImageMessageDto imageMessage = customerMessagePushDto.getImageMessage();
            try {
                WeMediaDto weMediaDto = weMaterialService.uploadTemporaryMaterial(imageMessage.getPic_url(),GroupMessageType.IMAGE.getMessageType(),
                        FileUtil.getName(imageMessage.getPic_url()));
                imageMessage.setMedia_id(weMediaDto.getMedia_id());
            } catch (Exception e) {
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgtype","image");
            jsonObject.put("image",imageMessage);
            list.add(jsonObject);
        }
        if (customerMessagePushDto.getMessageType().equals(GroupMessageType.LINK.getType())) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgtype","link");
            jsonObject.put("link",customerMessagePushDto.getLinkMessage());
            list.add(jsonObject);
        }

        if (customerMessagePushDto.getMessageType().equals(GroupMessageType.MINIPROGRAM.getType())) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgtype","miniprogram");
            jsonObject.put("miniprogram",customerMessagePushDto.getMiniprogramMessage());
            list.add(jsonObject);
        }

        if(customerMessagePushDto.getMessageType().equals(GroupMessageType.TEXT_IMAGE.getType())){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgtype","image");
            jsonObject.put("image",customerMessagePushDto.getImageMessage());
            list.add(jsonObject);

            weCustomerMessagePushDto.setText(customerMessagePushDto.getTextMessage());
        }

        if(customerMessagePushDto.getMessageType().equals(GroupMessageType.VIDEO.getType())){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgtype","video");
            jsonObject.put("video",customerMessagePushDto.getVideoDto());
            list.add(jsonObject);
        }
        weCustomerMessagePushDto.setAttachments(list);
    }

}
