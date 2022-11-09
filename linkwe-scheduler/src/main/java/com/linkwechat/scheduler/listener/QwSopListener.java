package com.linkwechat.scheduler.listener;

import com.alibaba.fastjson.JSONObject;
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






}
