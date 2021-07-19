package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerMessagePushClient;
import com.linkwechat.wecom.domain.WeCustomerMessage;
import com.linkwechat.wecom.domain.WeCustomerMessageOriginal;
import com.linkwechat.wecom.domain.dto.message.*;
import com.linkwechat.wecom.domain.vo.CustomerMessagePushVo;
import com.linkwechat.wecom.mapper.WeCustomerMessageMapper;
import com.linkwechat.wecom.mapper.WeCustomerMessageOriginalMapper;
import com.linkwechat.wecom.mapper.WeCustomerMessgaeResultMapper;
import com.linkwechat.wecom.service.IWeCustomerMessageOriginalService;
import com.linkwechat.wecom.service.IWeCustomerMessageService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 群发消息 原始数据信息表 we_customer_messageOriginal
 *
 * @author kewen
 * @date 2020-12-12
 */
@SuppressWarnings("all")
@Service
public class WeCustomerMessageOriginalServiceImpl extends ServiceImpl<WeCustomerMessageOriginalMapper, WeCustomerMessageOriginal> implements IWeCustomerMessageOriginalService {

    @Autowired
    private WeCustomerMessageOriginalMapper weCustomerMessageOriginalMapper;

    @Autowired
    private WeCustomerMessagePushClient weCustomerMessagePushClient;

    @Autowired
    private WeCustomerMessgaeResultMapper weCustomerMessgaeResultMapper;

    @Autowired
    private IWeCustomerMessageService weCustomerMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WeCustomerMessageMapper customerMessageMapper;

    @Override
    public List<CustomerMessagePushVo> customerMessagePushs(String sender, String content, String pushType, String beginTime, String endTime) {
        return weCustomerMessageOriginalMapper.selectCustomerMessagePushs(sender, content, pushType, beginTime, endTime);
    }

    @Override
    public CustomerMessagePushVo CustomerMessagePushDetail(Long messageId) {


        CustomerMessagePushVo customerMessagePushDetail = weCustomerMessageOriginalMapper.findCustomerMessagePushDetail(messageId);

        //检查是否已经同步发送结果
        if( weCustomerMessgaeResultMapper.checkSendStatus(messageId)==0){
            //拉取消息发送结果
            CompletableFuture.runAsync(() -> syncSendResult(customerMessagePushDetail.getMsgid(), messageId));
        }

        return customerMessagePushDetail;
    }

    @Override
    public void asyncResult(AsyncResultDto asyncResultDto) throws JsonProcessingException {
        String msgid=new ObjectMapper().writeValueAsString(asyncResultDto.getMsgids());
        syncSendResult(msgid, asyncResultDto.getMessageId());
    }

    @Override
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
        this.save(original);
        return messageOriginalId;
    }

    public void syncSendResult(String msgid, Long messageId) {

        AtomicInteger atomicInteger = new AtomicInteger();

        if (StringUtils.isNotEmpty(msgid)) {

            List<String> msgIds = null;

            try {

                msgIds = objectMapper.readValue(msgid, new TypeReference<List<String>>() {
                });

            } catch (JsonProcessingException e) {

                e.printStackTrace();

            }

            if (CollectionUtils.isNotEmpty(msgIds)) {

                msgIds.forEach(m -> {

                    QueryCustomerMessageStatusResultDataObjectDto dataObjectDto = new QueryCustomerMessageStatusResultDataObjectDto();

                    dataObjectDto.setMsgid(m);

                    //拉取发送结果
                    QueryCustomerMessageStatusResultDto queryCustomerMessageStatusResultDto = weCustomerMessagePushClient.queryCustomerMessageStatus(dataObjectDto);

                    if (WeConstans.WE_SUCCESS_CODE.equals(queryCustomerMessageStatusResultDto.getErrcode())) {


                        List<DetailMessageStatusResultDto> detailList = queryCustomerMessageStatusResultDto.getDetail_list();



                        detailList.forEach(d -> {

//                            if (d.getStatus().equals(WeConstans.sendMessageStatusEnum.SEND.getStatus())) {
//
//                                atomicInteger.incrementAndGet();
//                                //更新消息发送状态
//                                customerMessageMapper.updateWeCustomerMessageCheckStatusById(messageId,WeConstans.sendMessageStatusEnum.SEND.getStatus());
//                            }

                            weCustomerMessgaeResultMapper.updateWeCustomerMessgaeResult(messageId, d.getChat_id(), d.getExternal_userid(), d.getStatus(), d.getSend_time());

                        });

                    }

                });

            }

        }

        //更新微信实际发送条数
        weCustomerMessageService.updateWeCustomerMessageActualSend(messageId, atomicInteger.get());
    }

}
