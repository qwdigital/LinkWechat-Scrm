package com.linkwechat.framework.listener;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.Threads;
import com.linkwechat.wecom.client.WeMessagePushClient;
import com.linkwechat.wecom.domain.WeChatContactMsg;
import com.linkwechat.wecom.domain.WeChatContactSensitiveMsg;
import com.linkwechat.wecom.domain.WeSensitive;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.message.TextMessageDto;
import com.linkwechat.wecom.mapper.WeChatContactSensitiveMsgMapper;
import com.linkwechat.wecom.mapper.WeSensitiveMapper;
import com.linkwechat.wecom.service.IWeChatContactMsgService;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import com.linkwechat.wecom.service.IWeSensitiveActHitService;
import com.linkwechat.wecom.service.IWeSensitiveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 会话存档会话审核订阅者
 * @date 2021/7/27 23:11
 **/
@Slf4j
@Component
public class ChatMsgCheckListener implements MessageListener {

    @Autowired
    private IWeSensitiveService weSensitiveService;
    @Autowired
    private WeSensitiveMapper weSensitiveMapper;
    @Autowired
    private IWeChatContactMsgService weChatContactMsgService;
    @Autowired
    private WeChatContactSensitiveMsgMapper weChatContactSensitiveMsgMapper;
    @Autowired
    private IWeCorpAccountService weCorpAccountService;
    @Autowired
    private WeMessagePushClient weMessagePushClient;
    @Autowired
    private IWeSensitiveActHitService weSensitiveActHitService;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        JSONObject jsonObject = JSONObject.parseObject(new String(message.getBody()));
        log.info(">>》》》 订阅消息,会话审核订阅者：message:{}", jsonObject.toJSONString());

        Threads.SINGLE_THREAD_POOL.submit(() -> msgContextHandle(jsonObject));
    }

    private void msgContextHandle(JSONObject jsonObject) {
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
        StringBuilder objectString = new StringBuilder(jsonObject.getString(msgtype));
        if("external_redpacket".equals(msgtype)){
            objectString.append(jsonObject.getString("redpacket"));
        }else if("docmsg".equals(msgtype)){
            objectString.append(jsonObject.getString("doc"));
        }else if("markdown".equals(msgtype)
                || "news".equals(msgtype)){
            objectString.append(jsonObject.getString("info"));
        }
        log.info("执行敏感词命中过滤,time=[{}]", System.currentTimeMillis());
        //获取所有的敏感词规则
        List<WeSensitive> allSensitiveRules = weSensitiveMapper.selectWeSensitiveList(new WeSensitive());
        //根据规则过滤命中
        if (CollectionUtils.isNotEmpty(allSensitiveRules)) {
            String finalContent = objectString.toString();
            allSensitiveRules.forEach(weSensitive -> {
                List<String> patternWords = Arrays.asList(weSensitive.getPatternWords().split(","));
                List<String> users = weSensitiveService.getScopeUsers(weSensitive.getAuditUserScope());
                //log.info("handleSensitiveHit: >>>>>>>>>>>>>>>>>>>>>>finalContent:{}, patternWords:{},users:{},from:{}", finalContent, JSONObject.toJSONString(patternWords),  JSONObject.toJSONString(users), fromId);
                if (StringUtils.isNotBlank(finalContent)
                        && !CollectionUtils.isEmpty(users)
                        && users.stream().anyMatch(fromId::equals)) {
                    WeChatContactSensitiveMsg wccsm = hitSensitive(patternWords, fromId, finalContent);
                    //log.info("handleSensitiveHit: >>>>>>>>>>>>>>>>>>>>>> wccsm:{}",JSONObject.toJSONString(wccsm));
                    if (wccsm != null) {
                        wccsm.setMsgId(msgId);
                        wccsm.setFromId(fromId);
                        wccsm.setMsgTime(new Date(jsonObject.getLong("msgtime")));
                        int result = weChatContactSensitiveMsgMapper.insert(wccsm);
                        if (result > 0) {
                            sendMessage(wccsm, weSensitive);
                        }
                    }
                }
            });
        }
    }

    private WeChatContactSensitiveMsg hitSensitive(List<String> patternWords, String user, String content) {
        WeChatContactSensitiveMsg weChatContactSensitiveMsg = new WeChatContactSensitiveMsg();
        String patternWordStr = patternWords.stream().filter(content.trim()::contains).collect(Collectors.joining(","));
        //log.info("hitSensitive: >>>>>>>>>>>>>>>>>>>>>> patternWordStr:{}",patternWordStr);
        if (StringUtils.isNotEmpty(patternWordStr)) {
            weChatContactSensitiveMsg.setContent(content);
            weChatContactSensitiveMsg.setSendStatus(0);
            weChatContactSensitiveMsg.setPatternWords(patternWordStr);
            return weChatContactSensitiveMsg;
        }
        return null;
    }

    private void sendMessage(WeChatContactSensitiveMsg wccsm, Object weSensitiveObj) {
        WeSensitive weSensitive = (WeSensitive) weSensitiveObj;
        if (weSensitive.getAlertFlag().equals(1)) {
            //发送消息通知给相应的审计人
            WeCorpAccount weCorpAccount = weCorpAccountService.findValidWeCorpAccount();
            String auditUserId = weSensitive.getAuditUserId();
            String content = "有消息触发敏感词，请登录系统及时处理!";
            TextMessageDto textMessageDto = new TextMessageDto();
            textMessageDto.setContent(content);
            WeMessagePushDto pushDto = new WeMessagePushDto();
            pushDto.setTouser(auditUserId);
            pushDto.setMsgtype(MessageType.TEXT.getMessageType());
            pushDto.setText(textMessageDto);
            pushDto.setAgentid(Integer.parseInt(weCorpAccount.getAgentId()));
            weMessagePushClient.sendMessageToUser(pushDto, weCorpAccount.getAgentId());
            wccsm.setSendStatus(1);
            weChatContactSensitiveMsgMapper.updateById(wccsm);
        }
    }
}
