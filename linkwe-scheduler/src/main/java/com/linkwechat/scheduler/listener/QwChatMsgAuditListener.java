package com.linkwechat.scheduler.listener;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeChatContactMsg;
import com.linkwechat.service.IWeChatContactMsgService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author danmo
 * @description 会话存档消息监听
 * @date 2022/5/6 15:39
 **/
@Slf4j
@Component
public class QwChatMsgAuditListener {

    @Autowired
    private IWeChatContactMsgService weChatContactMsgService;

    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.chat-msg-audit:Qu_ChatMsgAudit}")
    public void subscribe(String msg, Channel channel, Message message) {
        try {
            log.info("会话存档消息监听：msg:{}",msg);

            msgContextHandle(JSONObject.parseObject(msg));

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            log.error("会话存档消息监听-消息处理失败 msg:{},error:{}",msg,e);
        }
    }


    private void msgContextHandle(JSONObject jsonObject){
        WeChatContactMsg weChatContactMsg = new WeChatContactMsg();
        weChatContactMsg.setMsgId(jsonObject.getString("msgid"));
        weChatContactMsg.setFromId(jsonObject.getString("from"));
        weChatContactMsg.setToList(CollectionUtil.join(jsonObject.getJSONArray("tolist"), ","));
        weChatContactMsg.setAction(jsonObject.getString("action"));
        weChatContactMsg.setRoomId(jsonObject.getString("roomid"));
        weChatContactMsg.setMsgType(jsonObject.getString("msgtype"));
        weChatContactMsg.setMsgTime(new Date(jsonObject.getLong("msgtime") == null? jsonObject.getLong("time") : jsonObject.getLong("msgtime")));
        weChatContactMsg.setSeq(jsonObject.getLong("seq"));
        if(jsonObject.getString("user") != null){
            weChatContactMsg.setFromId(jsonObject.getString("user"));
            weChatContactMsg.setToList(jsonObject.getString("user"));
        }
        String objectString = jsonObject.getString(jsonObject.getString("msgtype"));
        if(StringUtils.isNotEmpty(objectString)){
            weChatContactMsg.setContact(objectString);
        }else if("external_redpacket".equals(weChatContactMsg.getMsgType())){
            weChatContactMsg.setContact(jsonObject.getString("redpacket"));
        }else if("docmsg".equals(weChatContactMsg.getMsgType())){
            weChatContactMsg.setContact(jsonObject.getString("doc"));
        }else if("markdown".equals(weChatContactMsg.getMsgType())
                || "news".equals(weChatContactMsg.getMsgType())){
            weChatContactMsg.setContact(jsonObject.getString("info"));
        }
        if(!weChatContactMsg.getMsgId().contains("external")){
            weChatContactMsg.setIsExternal(1);
        }
        LambdaQueryWrapper<WeChatContactMsg> wrapper = new LambdaQueryWrapper<WeChatContactMsg>()
                .eq(WeChatContactMsg::getMsgId, weChatContactMsg.getMsgId())
                .eq(WeChatContactMsg::getFromId, weChatContactMsg.getFromId());
        weChatContactMsgService.saveOrUpdate(weChatContactMsg,wrapper);
    }
}
