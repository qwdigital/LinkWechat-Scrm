package com.linkwechat.scheduler.listener;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeSopChange;
import com.linkwechat.domain.groupchat.vo.LinkGroupChatListVo;
import com.linkwechat.domain.sop.dto.WeSopBaseDto;
import com.linkwechat.scheduler.service.SopTaskService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * sop相关监听
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class QwSopListener {

    @Autowired
    private SopTaskService abstractSopTaskService;



    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.sop:Qu_Sop}")
    public void sopSubscribe(String msg, Channel channel, Message message){

        try {
            log.info("sop任务构建：msg:{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            WeSopBaseDto weSopBaseDto = JSONObject.parseObject(msg, WeSopBaseDto.class);
            abstractSopTaskService.createOrUpdateSop(weSopBaseDto);

        }catch (Exception e){

            log.error("sop任务构建-消息处理失败 msg:{},error:{}", msg, e);
        }

    }



    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.sync.sop:Qu_NewWeCustomerSopQu}")
    public void builderNewWeCustomer(String msg, Channel channel, Message message){

        try {
            log.info("新加入的客户加入新客sop：msg:{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            WeCustomer weCustomer = JSONObject.parseObject(msg, WeCustomer.class);
          abstractSopTaskService.builderNewWeCustomer(weCustomer);

        }catch (Exception e){

            log.error("新加入的客户加入新客sop-消息处理失败 msg:{},error:{}", msg, e);
        }

    }



    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.sync.sop:Qu_Chnage_WeCustomerSopQu}")
    public void handleChangeSop(String msg, Channel channel, Message message){

        try {
            log.info("客户转入sop逻辑处理：msg:{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            WeSopChange weSopChange = JSONObject.parseObject(msg, WeSopChange.class);
            abstractSopTaskService.handleChangeSop(weSopChange);

        }catch (Exception e){

            log.error("客户转入sop逻辑处理-消息处理失败 msg:{},error:{}", msg, e);
        }

    }



    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.sync.sop:Qu_NewWeGroupSopQu}")
    public void builderNewGroup(String msg, Channel channel, Message message){

        try {
            log.info("新建的群加入新客群sop：msg:{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            LinkGroupChatListVo linkGroupChatListVo = JSONObject.parseObject(msg, LinkGroupChatListVo.class);
            abstractSopTaskService.builderNewGroup(linkGroupChatListVo);
        }catch (Exception e){

            log.error("新建的群加入新客群sop-消息处理失败 msg:{},error:{}", msg, e);
        }

    }





}
