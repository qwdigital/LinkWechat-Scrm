package com.linkwechat.framework.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 会话存档会话审核订阅者
 * @date 2021/7/27 23:11
 **/
@Slf4j
@Component
public class ChatMsgCheckListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {

        log.info(">> 订阅消息,会话审核订阅者：{}", message);
        String body = new String(message.getBody());
        //渠道名称
        String topic = new String(pattern);
        System.out.println(body);
        System.out.println(topic);
    }
}
