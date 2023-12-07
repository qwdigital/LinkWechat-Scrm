package com.linkwechat.scheduler.listener;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.domain.task.query.WeTasksRequest;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.service.IWeTasksService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 待办任务监听
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/21 18:36
 */
@Slf4j
@Component
public class WeTasksListener {

    @Resource
    private IWeTasksService weTasksService;


    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.delay.we-tasks:Qu_Tasks}")
    public void subscribe(String msg, Channel channel, Message message) {
        log.info("待办任务监听:{}", msg);
        try {
            WeTasksRequest weTasksRequest = JSONObject.parseObject(msg, WeTasksRequest.class);
            weTasksService.handlerWeTasks(weTasksRequest);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("待办任务监听-消息处理失败 msg:{},error:{}", msg, e);
        }
    }
}
