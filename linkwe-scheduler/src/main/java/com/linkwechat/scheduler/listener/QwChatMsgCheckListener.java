package com.linkwechat.scheduler.listener;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeChatContactSensitiveMsg;
import com.linkwechat.domain.WeSensitive;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.service.IWeChatContactSensitiveMsgService;
import com.linkwechat.service.IWeSensitiveActHitService;
import com.linkwechat.service.IWeSensitiveService;
import com.linkwechat.service.QwAppSendMsgService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 会话存档消息审核监听
 * @date 2022/5/6 15:39
 **/
@Slf4j
@Component
public class QwChatMsgCheckListener {

    @Autowired
    private IWeSensitiveService weSensitiveService;

    @Autowired
    private IWeSensitiveActHitService weSensitiveActHitService;

    @Autowired
    private IWeChatContactSensitiveMsgService weChatContactSensitiveMsgService;

    @Autowired
    private QwAppSendMsgService qwAppSendMsgService;

    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.chat-msg-check:Qu_ChatMsgCheck}")
    public void subscribe(String msg, Channel channel, Message message) throws IOException {
        try {
            log.info("会话存档消息审核监听：msg:{}", msg);

            msgContextHandle(JSONObject.parseObject(msg));

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("会话存档消息监听-消息处理失败 msg:{},error:{}", msg, e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
        }
    }


    public void msgContextHandle(JSONObject jsonObject) {
        if(jsonObject.get("action") != null && jsonObject.getString("action").equals("switch")){
            return;
        }
        String msgType = jsonObject.getString("msgtype");
        if ("external_redpacket".equals(msgType)
                || "redpacket".equals(msgType)
                || "card".equals(msgType)) {
            weSensitiveActHitService.hitWeSensitiveAct(jsonObject);
        } else {
            handleSensitiveHit(jsonObject);
        }
    }

    private void handleSensitiveHit(JSONObject jsonObject) {
        String fromId = jsonObject.getString("from");
        String msgId = jsonObject.getString("msgid");
        String msgtype = jsonObject.getString("msgtype");
        StringBuilder objectString = new StringBuilder(jsonObject.getString(msgtype) == null ? "" : jsonObject.getString(msgtype));
        if ("external_redpacket".equals(msgtype)) {
            objectString.append(jsonObject.getString("redpacket"));
        } else if ("docmsg".equals(msgtype)) {
            objectString.append(jsonObject.getString("doc"));
        } else if ("markdown".equals(msgtype)
                || "news".equals(msgtype)) {
            objectString.append(jsonObject.getString("info"));
        }
        //获取所有的敏感词规则
        List<WeSensitive> allSensitiveRules = weSensitiveService.selectWeSensitiveList(WeSensitive.builder()
                        .alertFlag(1)
                .build());
        //根据规则过滤命中
        if (CollectionUtils.isNotEmpty(allSensitiveRules)) {
            String finalContent = objectString.toString();
            allSensitiveRules.forEach(weSensitive -> {
                List<String> patternWords = Arrays.asList(weSensitive.getPatternWords().split(","));
                List<String> users = weSensitiveService.getScopeUsers(weSensitive.getAuditUserScope());
                if (StringUtils.isNotBlank(finalContent)
                        && ((CollectionUtils.isNotEmpty(users) && users.stream().anyMatch(user -> user.contains(fromId)))
                        || StringUtils.isEmpty(weSensitive.getAuditUserScope()))) {
                    WeChatContactSensitiveMsg wccsm = hitSensitive(patternWords, finalContent);
                    if (wccsm != null) {
                        wccsm.setMsgId(msgId);
                        wccsm.setFromId(fromId);
                        wccsm.setMsgTime(new Date(jsonObject.getLong("msgtime")));
                        if (weChatContactSensitiveMsgService.save(wccsm)) {
                            QwAppMsgBody qwAppMsgBody = new QwAppMsgBody();
                            //发送人指定员工
                            qwAppMsgBody.setCorpUserIds(Collections.singletonList(weSensitive.getAuditUserId()));
                            //设置消息模板
                            WeMessageTemplate template = new WeMessageTemplate();
                            template.setMsgType(MessageType.TEXT.getMessageType());
                            template.setContent("有消息触发敏感词，请登录系统及时处理!");
                            qwAppMsgBody.setMessageTemplates(template);
                            qwAppSendMsgService.appMsgSend(qwAppMsgBody);
                        }
                    }
                }
            });
        }
    }

    private WeChatContactSensitiveMsg hitSensitive(List<String> patternWords, String content) {
        WeChatContactSensitiveMsg weChatContactSensitiveMsg = new WeChatContactSensitiveMsg();
        String patternWordStr = patternWords.stream().filter(content.trim()::contains).collect(Collectors.joining(","));
        if (StringUtils.isNotEmpty(patternWordStr)) {
            weChatContactSensitiveMsg.setContent(content);
            weChatContactSensitiveMsg.setSendStatus(0);
            weChatContactSensitiveMsg.setPatternWords(patternWordStr);
            return weChatContactSensitiveMsg;
        }
        return null;
    }
}
