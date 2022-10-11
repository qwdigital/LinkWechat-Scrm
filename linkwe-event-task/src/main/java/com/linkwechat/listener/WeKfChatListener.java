package com.linkwechat.listener;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.enums.WeKfMsgTypeEnum;
import com.linkwechat.domain.kf.query.WeKfChatMsgListQuery;
import com.linkwechat.factory.WeKfStrategyBeanFactory;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * @author danmo
 * @Description 回调通知处理
 * @date 2021/11/21 19:57
 **/
@Component
@Slf4j
public class WeKfChatListener {

    @Autowired
    private WeKfStrategyBeanFactory weKfStrategyBeanFactory;

    @RabbitListener(queues = "${wecom.mq.queue.kf-chat-msg:Qu_KfChatMsg}")
    @RabbitHandler
    public void subscribe(String msg, Channel channel, Message message) throws IOException {
        try {
            log.info("客服消息：msg:{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            WeKfChatMsgListQuery query = JSONObject.parseObject(msg, WeKfChatMsgListQuery.class);
            kfMsgHandler(query);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("客服消息-消息丢失,重发消息>>>>>>>>>>msg:{}", msg);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
        } finally {
            SecurityContextHolder.remove();
        }
    }

    private void kfMsgHandler(WeKfChatMsgListQuery query) {
        for (JSONObject msgInfo : query.getMsgList()) {
            String msgtype = msgInfo.getString("msgtype");
            if(ObjectUtil.equal(WeKfMsgTypeEnum.EVENT.getType(), msgtype)){
                JSONObject eventMsg = msgInfo.getJSONObject(msgtype);
                eventMsg.put("corpId",query.getCorpId());
                eventMsg.put("msgid",msgInfo.getString("msgid"));
                eventMsg.put("origin",msgInfo.getString("origin"));
                eventMsg.put("send_time",new Date(msgInfo.getLong("send_time") * 1000));
                weKfStrategyBeanFactory.getResource(eventMsg.getString("event_type"),eventMsg);
            }else {
                msgInfo.put("corpId",query.getCorpId());
                weKfStrategyBeanFactory.getResource("kf_msg",msgInfo);
            }
        }
    }
}
