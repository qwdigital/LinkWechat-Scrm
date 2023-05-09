package com.linkwechat.scheduler.listener;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.enums.WelcomeMsgTypeEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.scheduler.service.IWelcomeMsgService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 欢迎语消息监听
 * @date 2022/4/3 15:39
 **/
@Slf4j
@Component
public class QwWelcomeMsgListener {

    @Autowired
    private Map<String, IWelcomeMsgService> welcomeMsgMap = new ConcurrentHashMap<>();

    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.customer-welcome-msg:Qu_CustomerWelcomeMsg}")
    public void subscribe(String msg, Channel channel, Message message) throws IOException {
        try {
            log.info("客户欢迎语消息监听：msg:{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            WeBackCustomerVo query = JSONObject.parseObject(msg, WeBackCustomerVo.class);
            factory(query.getState()).msgHandle(query);
        } catch (Exception e) {
            log.error("客户欢迎语消息-消息处理失败 msg:{},error:{}", msg, e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
        }
    }


    /**
     * 渠道欢迎语路由
     */
    /*private final Map<String, String> route = new HashMap<String, String>() {
        {
            put("we_qr", "weQrCodeMsgServiceImpl");
            put("we_lxqr", "weLxQrCodeMsgServiceImpl");
            put("we_sc_conf", "weStoreQrCodeMsgServiceImpl");
            put("we_kn_code", "weKnowCustomerMsgServiceImpl");
            put("we_xklq", "weXklqQrCodeMsgServiceImpl");
            put("fis-", "weFissionMsgServiceImpl");
            put("default", "weDefaultQrCodeMsgServiceImpl");
        }
    };*/

    private final Map<String, String> route = WelcomeMsgTypeEnum.getMap();

    private IWelcomeMsgService factory(String state) {

        if (StringUtils.isEmpty(state) || route.keySet().stream().noneMatch(state::startsWith)) {
            return welcomeMsgMap.get("weDefaultQrCodeMsgServiceImpl");
        }
        String key = route.keySet().stream().filter(state::startsWith).collect(Collectors.joining());

        String resolveClass = route.get(key);

        if (!welcomeMsgMap.containsKey(resolveClass)) {
            return welcomeMsgMap.get("weDefaultQrCodeMsgServiceImpl");
        }
        return welcomeMsgMap.get(resolveClass);
    }
}
