package com.linkwechat.scheduler.listener;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.service.IWeGroupMessageTemplateService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 企微群发消息监听
 * @date 2022/4/3 15:39
 **/
@Slf4j
@Component
public class QwGroupMsgListener {

    @Autowired
    private IWeGroupMessageTemplateService weGroupMessageTemplateService;

    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.group-msg:Qu_GroupMsg}")
    public void normalMsgSubscribe(String msg, Channel channel, Message message) {
        try {
            log.info("正常群发消息监听：msg:{}", msg);
            WeAddGroupMessageQuery query = JSONObject.parseObject(msg, WeAddGroupMessageQuery.class);
            weGroupMessageTemplateService.groupMessageTaskHandler(query);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("正常群发消息监听-消息处理失败 msg:{},error:{}", msg, e);
        }
    }

    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.delay-group-msg:Qu_DelayGroupMsg}")
    public void subscribe(String msg, Channel channel, Message message) {
        try {
            log.info("延时群发消息监听：msg:{}", msg);
            WeAddGroupMessageQuery query = JSONObject.parseObject(msg, WeAddGroupMessageQuery.class);
            weGroupMessageTemplateService.groupMessageTaskHandler(query);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("延时群发消息监听-消息处理失败 msg:{},error:{}", msg, e);
        }
    }




}
