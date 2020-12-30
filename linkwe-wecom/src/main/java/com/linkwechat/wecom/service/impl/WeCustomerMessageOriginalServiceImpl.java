package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerMessagePushClient;
import com.linkwechat.wecom.domain.WeCustomerMessageOriginal;
import com.linkwechat.wecom.domain.dto.message.DetailMessageStatusResultDto;
import com.linkwechat.wecom.domain.dto.message.QueryCustomerMessageStatusResultDataObjectDto;
import com.linkwechat.wecom.domain.dto.message.QueryCustomerMessageStatusResultDto;
import com.linkwechat.wecom.domain.vo.CustomerMessagePushVo;
import com.linkwechat.wecom.mapper.WeCustomerMessageOriginalMapper;
import com.linkwechat.wecom.mapper.WeCustomerMessgaeResultMapper;
import com.linkwechat.wecom.service.IWeCustomerMessageOriginalService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 群发消息 原始数据信息表 we_customer_messageOriginal
 *
 * @author kewen
 * @date 2020-12-12
 */
@Service
public class WeCustomerMessageOriginalServiceImpl extends ServiceImpl<WeCustomerMessageOriginalMapper, WeCustomerMessageOriginal> implements IWeCustomerMessageOriginalService {

    @Autowired
    private WeCustomerMessageOriginalMapper weCustomerMessageOriginalMapper;

    @Autowired
    private WeCustomerMessagePushClient weCustomerMessagePushClient;

    @Autowired
    private WeCustomerMessgaeResultMapper weCustomerMessgaeResultMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public int saveWeCustomerMessageOriginal(WeCustomerMessageOriginal weCustomerMessageOriginal) {
        return weCustomerMessageOriginalMapper.insert(weCustomerMessageOriginal);
    }

    @Override
    public List<CustomerMessagePushVo> customerMessagePushs(String sender, String content, String pushType, String beginTime, String endTime) {
        return weCustomerMessageOriginalMapper.selectCustomerMessagePushs(sender, content, pushType, beginTime, endTime);
    }

    @Override
    public CustomerMessagePushVo CustomerMessagePushDetail(Long messageId)  {


        CustomerMessagePushVo customerMessagePushDetail = weCustomerMessageOriginalMapper.findCustomerMessagePushDetail(messageId);

        //拉取消息发送结果
       CompletableFuture.runAsync(()->{

            String msgid = customerMessagePushDetail.getMsgid();

            if(StringUtils.isNotEmpty(msgid)){

                List<String> msgIds = null;
                try {
                    msgIds = objectMapper.readValue(msgid,new TypeReference<List<String>>() { });
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                if(CollectionUtils.isNotEmpty(msgIds)){
                    msgIds.forEach(m->{

                        QueryCustomerMessageStatusResultDataObjectDto dataObjectDto=new QueryCustomerMessageStatusResultDataObjectDto();

                        dataObjectDto.setMsgid(m);

                        //拉取发送结果
                        QueryCustomerMessageStatusResultDto queryCustomerMessageStatusResultDto = weCustomerMessagePushClient.queryCustomerMessageStatus(dataObjectDto);
                        if (WeConstans.WE_SUCCESS_CODE.equals(queryCustomerMessageStatusResultDto.getErrcode())) {


                            List<DetailMessageStatusResultDto> detailList = queryCustomerMessageStatusResultDto.getDetail_list();

                            detailList.forEach(d-> weCustomerMessgaeResultMapper.updateWeCustomerMessgaeResult(messageId,d.getChat_id(),d.getExternal_userid(),d.getStatus(),d.getSend_time()));

                        }

                    });
                }

            }

        });


        return weCustomerMessageOriginalMapper.findCustomerMessagePushDetail(messageId);
    }

}
