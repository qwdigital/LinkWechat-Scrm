package com.linkwechat.listener;

import cn.hutool.core.util.XmlUtil;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.factory.WeCallBackEventFactory;
import com.linkwechat.service.IWeCorpAccountService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author danmo
 * @Description 回调通知处理
 * @date 2021/11/21 19:57
 **/
@Component
@Slf4j
public class WeCallBackEventListener {

    @Autowired
    private Map<String, WeCallBackEventFactory> weCallBackEventFactoryMap = new ConcurrentHashMap<>();

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    /**
     * 企微回调事件类型路由
     */
    private final Map<String, String> eventRoute = new HashMap<String, String>() {
        {
            //成员事件
            put("change_contact", "weEventChangeContactImpl");
            //异步任务完成通知
            put("batch_job_result", "weEventBatchJobResultImpl");
            //外部联系人事件
            put("change_external_contact", "weEventChangeExternalContactImpl");
            //客户群事件
            put("change_external_chat", "weEventChangeExternalChatImpl");
            //客户标签事件
            put("change_external_tag", "weEventChangeExternalTagImpl");
            //客服消息事件
            put("kf_msg_or_event", "weKfMsgOrEventImpl");
            //产生会话回调事件
            put("msgaudit_notify", "weMsgAuditNotifyImpl");
            //应用管理员变更通知
            put("change_app_admin", "weChangeAppAdminImpl");
            //直播回掉
            put("living_status_change", "weCallBackLiveImpl");
        }
    };

    @RabbitListener(queues = "${wecom.mq.callback-queue:Qu_CallBack}")
    @RabbitHandler
    public void onMessage(String msg, Channel channel, Message message) throws IOException {
        try {
            log.info("企微回调消息监听：msg:{}",msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            WeBackBaseVo weBackBaseVo = XmlUtil.xmlToBean(XmlUtil.parseXml(msg).getFirstChild(), WeBackBaseVo.class);
            WeCallBackEventFactory factory = factory(weBackBaseVo.getEvent());
            if(factory != null){
                factory.eventHandle(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("回调通知处理-消息丢失,重发消息>>>>>>>>>>msg:{}",msg);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);
        }finally {
            SecurityContextHolder.remove();
        }
    }

    private WeCallBackEventFactory factory(String eventType) {
        if (!eventRoute.containsKey(eventType)) {
            return null;
        }
        String resolveClass = eventRoute.get(eventType);
        if (!weCallBackEventFactoryMap.containsKey(resolveClass)) {
            return null;
        }
        return weCallBackEventFactoryMap.get(resolveClass);
    }

}
