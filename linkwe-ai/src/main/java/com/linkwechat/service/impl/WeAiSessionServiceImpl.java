package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.AiMessage;
import com.linkwechat.domain.WeAiMsg;
import com.linkwechat.domain.WeAiMsgQuery;
import com.linkwechat.domain.WeAiMsgVo;
import com.linkwechat.service.HunYuanService;
import com.linkwechat.service.IWeAiMsgService;
import com.linkwechat.service.IWeAiSessionService;
import com.linkwechat.utils.WeAiSessionUtil;
import com.tencentcloudapi.hunyuan.v20230901.models.ChatStdResponse;
import com.tencentcloudapi.hunyuan.v20230901.models.Choice;
import com.tencentcloudapi.hunyuan.v20230901.models.Delta;
import com.tencentcloudapi.hunyuan.v20230901.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WeAiSessionServiceImpl implements IWeAiSessionService {

    @Autowired
    private IWeAiMsgService iWeAiMsgService;

    @Autowired
    private HunYuanService hunYuanService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Override
    public SseEmitter createSseConnect(String sessionId) {
        if (StringUtils.isEmpty(sessionId)) {
            sessionId = IdUtil.simpleUUID();
        }
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        SseEmitter sseEmitter = new SseEmitter(0L);
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(sessionId));

        WeAiSessionUtil.add(sessionId, sseEmitter);
        log.info("创建新的sse连接，当前session：{}", sessionId);

        try {
            sseEmitter.send(SseEmitter.event().id("sessionId").data(sessionId));
        } catch (IOException e) {
            log.error("SseEmitterServiceImpl[createSseConnect]: 创建长链接异常，客户端ID:{}", sessionId, e);
            throw new WeComException("创建连接异常！");
        }
        return sseEmitter;
    }

    @Override
    public void closeSseConnect(String sessionId) {
        WeAiSessionUtil.removeAndClose(sessionId);
    }

    @Override
    public void sendMsg(WeAiMsgQuery query) {
        SseEmitter sseEmitter = WeAiSessionUtil.get(query.getSessionId());
        if (Objects.nonNull(sseEmitter)) {
            sendAiMsg(query);
        } else {
            rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getAiMsgEx(), "", JSONObject.toJSONString(query));
        }
    }


    private Runnable completionCallBack(String sessionId) {
        return () -> {
            log.info("结束连接：{}", sessionId);
            WeAiSessionUtil.removeAndClose(sessionId);
        };
    }


    public void sendAiMsg(WeAiMsgQuery query) {
        if (Objects.isNull(query.getMsg())) {
            return;
        }
        List<AiMessage> aiMessageList = new ArrayList<>();

        List<WeAiMsg> aiLastMsgList = iWeAiMsgService.list(new LambdaQueryWrapper<WeAiMsg>()
                .eq(WeAiMsg::getSessionId, query.getSessionId())
                .orderByAsc(WeAiMsg::getId)
                .last(" limit 40"));

        if (CollectionUtil.isNotEmpty(aiLastMsgList)) {
            for (WeAiMsg aiMsg : aiLastMsgList) {
                AiMessage aiMessage = new AiMessage();
                aiMessage.setRole(aiMsg.getRole());
                aiMessage.setContent(aiMsg.getContent());
                aiMessageList.add(aiMessage);
            }
        }
        aiMessageList.add(query.getMsg());


        Message[] msgArray = aiMessageList.stream().map(item -> {
            Message message = new Message();
            message.setRole(item.getRole());
            message.setContent(item.getContent());
            return message;
        }).toArray(Message[]::new);


        WeAiMsg replyMsg = new WeAiMsg();
        replyMsg.setSessionId(query.getSessionId());
        StringBuilder replyContent = new StringBuilder();
        hunYuanService.sendMsg(msgArray, (data) -> {
            SseEmitter sseEmitter = WeAiSessionUtil.get(query.getSessionId());
            if (Objects.nonNull(sseEmitter)) {
                ChatStdResponse response = JSONObject.parseObject(data, ChatStdResponse.class);

                replyMsg.setMsgId(response.getId());
                replyMsg.setNote(response.getNote());
                replyMsg.setRequestId(response.getRequestId());
                replyMsg.setSendTime(new Date(response.getCreated() * 1000));
                try {
                    sseEmitter.send(SseEmitter.event().name("msg").data(response));
                } catch (IOException e) {
                    log.error("发送客户端异常 query：{}", JSONObject.toJSONString(query), e);
                }
                replyContent.append(Arrays.stream(response.getChoices()).map(Choice::getDelta).map(Delta::getContent).findFirst().orElse(""));
                String role = Arrays.stream(response.getChoices()).map(Choice::getDelta).map(Delta::getRole).findFirst().orElse("assistant");
                replyMsg.setRole(role);
            }
        });
        replyMsg.setContent(replyContent.toString());
        replyMsg.setUserId(SecurityUtils.getUserId());

        WeAiMsg sendMsg = new WeAiMsg();
        sendMsg.setSessionId(query.getSessionId());
        sendMsg.setSendTime(new Date());
        sendMsg.setRole(query.getMsg().getRole());
        sendMsg.setContent(query.getMsg().getContent());
        sendMsg.setUserId(SecurityUtils.getUserId());

        List<WeAiMsg> addMsgList = new ArrayList<>();
        addMsgList.add(sendMsg);
        addMsgList.add(replyMsg);
        iWeAiMsgService.saveBatch(addMsgList);
    }

    @Override
    public PageInfo<WeAiMsgVo> list(WeAiMsgQuery query) {
        PageInfo<WeAiMsgVo> pageInfo = new PageInfo<>();
        List<WeAiMsg> weAiMsgList =  iWeAiMsgService.getSessionList(SecurityUtils.getUserId());
        if(CollectionUtil.isNotEmpty(weAiMsgList)){
            List<WeAiMsgVo> weAiMsgVos = weAiMsgList.stream().map(item -> {
                WeAiMsgVo weAiMsgVo = new WeAiMsgVo();
                BeanUtil.copyProperties(item, weAiMsgVo);
                return weAiMsgVo;
            }).collect(Collectors.toList());
            pageInfo.setList(weAiMsgVos);
        }
        PageInfo<WeAiMsg> msgPageInfo = new PageInfo<>(weAiMsgList);
        pageInfo.setTotal(msgPageInfo.getTotal());
        return pageInfo;
    }

    @Override
    public List<WeAiMsgVo> getDetail(String sessionId) {
        List<WeAiMsg> list = iWeAiMsgService.list(new LambdaQueryWrapper<WeAiMsg>().eq(WeAiMsg::getSessionId, sessionId).eq(WeAiMsg::getUserId, SecurityUtils.getUserId())
                .orderByAsc(WeAiMsg::getId));
        if(CollectionUtil.isNotEmpty(list)){
            return list.stream().map(item -> {
                WeAiMsgVo weAiMsgVo = new WeAiMsgVo();
                BeanUtil.copyProperties(item, weAiMsgVo);
                return weAiMsgVo;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public SseEmitter createAndSendMsg(WeAiMsgQuery query) {
        if (StringUtils.isEmpty(query.getSessionId())) {
            query.setSessionId(IdUtil.simpleUUID());
        }
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        SseEmitter sseEmitter = new SseEmitter(30 * 1000L);
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(query.getSessionId()));

        WeAiSessionUtil.add(query.getSessionId(), sseEmitter);
        log.info("创建新的sse连接，当前session：{}", query.getSessionId());

        try {
            sseEmitter.send(SseEmitter.event().id("sessionId").data(query.getSessionId()));
            sendAiMsg(query);
        } catch (IOException e) {
            log.error("链接异常，sessionId:{}", query.getSessionId(), e);
            throw new WeComException("连接异常！");
        }
        return sseEmitter;
    }
}
