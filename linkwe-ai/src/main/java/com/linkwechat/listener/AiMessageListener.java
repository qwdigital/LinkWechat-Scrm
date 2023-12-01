package com.linkwechat.listener;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.domain.WeAiMsgQuery;
import com.linkwechat.service.IWeAiSessionService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AiMessageListener {

    @Autowired
    private IWeAiSessionService iWeAiSessionService;

    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.ai-msg:Qu_AiMsg}")
    public void subscribe(String msg, Channel channel, Message message) {
        try {
            log.info("发送AI消息：msg:{}", msg);

            iWeAiSessionService.sendAiMsg(JSONObject.parseObject(msg, WeAiMsgQuery.class));

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("发送AI消息监听-消息处理失败 msg:{},error:{}", msg, e.getMessage(), e);
        }

    }


}