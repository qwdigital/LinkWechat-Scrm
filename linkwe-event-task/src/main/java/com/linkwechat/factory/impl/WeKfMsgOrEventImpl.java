package com.linkwechat.factory.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.enums.WeKfMsgTypeEnum;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.kf.query.WeKfChatMsgListQuery;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.query.kf.WeKfGetMsgQuery;
import com.linkwechat.domain.wecom.vo.kf.WeKfGetMsgVo;
import com.linkwechat.factory.WeCallBackEventFactory;
import com.linkwechat.factory.WeStrategyBeanFactory;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.service.IWeKfEventMsgService;
import com.linkwechat.service.IWeKfMsgCursorService;
import com.linkwechat.service.IWeKfMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 客服事件
 * @date 2021/12/20 10:12
 **/
@Service
@Slf4j
public class WeKfMsgOrEventImpl implements WeCallBackEventFactory {

    @Autowired
    private IWeKfMsgCursorService weKfMsgCursorService;

    @Autowired
    private IWeKfMsgService weKfMsgService;

    @Autowired
    private IWeKfEventMsgService weKfEventMsgService;

    @Autowired
    private QwKfClient qwKfClient;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void eventHandle(String message) {
        WeBackBaseVo weBackBaseVo = XmlUtil.xmlToBean(XmlUtil.parseXml(message).getFirstChild(), WeBackBaseVo.class);
        String corpId = weBackBaseVo.getToUserName();
        String nextCursor = weKfMsgCursorService.getKfMsgCursor(corpId);
        //读取会话消息
        WeKfGetMsgQuery weKfGetMsgQuery = new WeKfGetMsgQuery();
        weKfGetMsgQuery.setCursor(nextCursor);
        weKfGetMsgQuery.setToken(weBackBaseVo.getToken());
        WeKfGetMsgVo weKfGetMsgVo = qwKfClient.getSessionMsg(weKfGetMsgQuery).getData();
        nextCursor = weKfGetMsgVo.getNextCursor();
        weKfMsgCursorService.saveKfMsgCursor(corpId,nextCursor);
        //消息入库
        List<JSONObject> msgList = weKfGetMsgVo.getMsgList();
        List<JSONObject> weKfMsgList = msgList.stream()
                .filter(msgInfo -> !ObjectUtil.equal(WeKfMsgTypeEnum.EVENT.getType(), msgInfo.getString("msgtype")))
                .collect(Collectors.toList());
        weKfMsgService.saveMsgOrEvent(weKfMsgList);

        //事件消息入库
        List<JSONObject> weKfEventMsgList = msgList.stream()
                .filter(msgInfo -> ObjectUtil.equal(WeKfMsgTypeEnum.EVENT.getType(), msgInfo.getString("msgtype")))
                .collect(Collectors.toList());
        weKfEventMsgService.saveEventMsg(weKfEventMsgList);

        //处理会话逻辑
        WeKfChatMsgListQuery query = new WeKfChatMsgListQuery();
        query.setCorpId(corpId);
        query.setMsgList(msgList);
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeKfChatMsgEx(),rabbitMQSettingConfig.getWeKfChatMsgRk(),JSONObject.toJSONString(query));
    }

}
