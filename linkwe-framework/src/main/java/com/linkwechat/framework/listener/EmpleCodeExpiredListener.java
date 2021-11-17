package com.linkwechat.framework.listener;


import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.wecom.client.WeExternalContactClient;
import com.linkwechat.wecom.domain.WeEmpleCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.nio.charset.StandardCharsets;

/**
 * @author danmo
 * @description redis 键值过期监听
 * @date 2021/2/8 17:24
 **/
public class EmpleCodeExpiredListener extends KeyExpirationEventMessageListener {
    @Autowired
    private WeExternalContactClient weExternalContactClient;

    public EmpleCodeExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("过期key:" + message.toString());
        if(message.toString().contains(WeConstans.WE_EMPLE_CODE_KEY)){
            //weExternalContactClient.delContactWay(message.toString().split(":")[1]);
        }
    }
}
