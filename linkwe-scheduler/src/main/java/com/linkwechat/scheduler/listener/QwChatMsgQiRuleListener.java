package com.linkwechat.scheduler.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.WeQiRuleMsg;
import com.linkwechat.domain.qirule.query.WeQiRuleListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleListVo;
import com.linkwechat.service.*;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author danmo
 * @description 会话存档规则消息监听
 * @date 2023/5/6 15:39
 **/
@Slf4j
@Component
public class QwChatMsgQiRuleListener {

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private IWeGroupMemberService weGroupMemberService;

    @Autowired
    private IWeQiRuleService weQiRuleService;

    @Autowired
    private IWeQiRuleMsgService weQiRuleMsgService;

    //！！！为了保证消息顺序性，不可有多个队列监听
    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.chat-msg-qi-rule:Qu_ChatMsgQiRule}",exclusive = true, concurrency = "1")
    public void subscribe(String msg, Channel channel, Message message) throws IOException {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("会话规则拦截消息：msg:{}", msg);

            msgQiRuleHandler(JSONObject.parseObject(msg));


        } catch (Exception e) {
            log.error("会话存档消息监听-消息处理失败 msg:{},error:{}", msg, e);
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
        }
    }


    private void msgQiRuleHandler(JSONObject jsonObject) {

        String fromId = jsonObject.getString("from");
        String msgId = jsonObject.getString("msgid");
        String roomId = jsonObject.getString("roomid");
        Date sendTime = new Date(jsonObject.getLong("msgtime") == null ? jsonObject.getLong("time") : jsonObject.getLong("msgtime"));

        //非外部会话忽略
        if (!msgId.contains("external")) {
            return;
        }
        //判断是否为群聊
        if (StringUtils.isEmpty(roomId)) {
            String receiveId = jsonObject.getJSONArray("tolist").getString(0);
            externalCustomerMsgHandler(msgId, fromId, receiveId, sendTime);
        } else {
            externalGroupChatMsgHandler(msgId, fromId, roomId, sendTime);
        }

    }

    //外部联系人会话处理
    private void externalCustomerMsgHandler(String msgId, String fromId, String receiveId, Date sendTime) {
        //发起为是否为客户
        if (isExternal(fromId)) {
            //匹配规则
            WeQiRuleListQuery ruleListQuery = new WeQiRuleListQuery();
            ruleListQuery.setChatType(Lists.newArrayList(1, 2));
            ruleListQuery.setUserIds(receiveId);
            ruleListQuery.setWorkCycle(DateUtil.dayOfWeek(new Date()));
            ruleListQuery.setFormatTime(DateUtil.date());
            List<WeQiRuleListVo> qiRuleList = weQiRuleService.getQiRuleListByUserId(ruleListQuery);
            if (CollectionUtil.isNotEmpty(qiRuleList)) {
                WeQiRuleListVo weQiRuleVo = qiRuleList.get(0);
                Integer timeOut = weQiRuleVo.getTimeOut();

                WeQiRuleMsg weQiRuleMsg = weQiRuleMsgService.getOne(new LambdaQueryWrapper<WeQiRuleMsg>()
                        .eq(WeQiRuleMsg::getFromId, fromId)
                        .eq(WeQiRuleMsg::getReceiveId, receiveId)
                        .eq(WeQiRuleMsg::getChatType, 1)
                        .eq(WeQiRuleMsg::getReplyStatus,1)
                        .last("limit 1")
                        .orderByDesc(BaseEntity::getCreateTime));

                WeQiRuleMsg ruleMsg = new WeQiRuleMsg();
                ruleMsg.setRuleId(weQiRuleVo.getId());
                ruleMsg.setMsgId(msgId);
                ruleMsg.setTimeOut(DateUtil.offsetMinute(sendTime,timeOut));
                ruleMsg.setChatType(1);
                ruleMsg.setFromId(fromId);
                ruleMsg.setReceiveId(receiveId);
                ruleMsg.setSendTime(sendTime);
                if (Objects.isNull(weQiRuleMsg)) {
                    weQiRuleMsgService.save(ruleMsg);
                } else {
                    Long between = DateUtil.between(weQiRuleMsg.getSendTime(), DateUtil.date(), DateUnit.MINUTE, true);
                    if (between > timeOut) {
                        weQiRuleMsgService.save(ruleMsg);
                    } else {
                        ruleMsg.setId(weQiRuleMsg.getId());
                        weQiRuleMsgService.updateById(ruleMsg);
                    }
                }


            }
        } else {
            WeQiRuleMsg weQiRuleMsg = weQiRuleMsgService.getOne(new LambdaQueryWrapper<WeQiRuleMsg>()
                    .eq(WeQiRuleMsg::getFromId, receiveId)
                    .eq(WeQiRuleMsg::getReceiveId, fromId)
                    .eq(WeQiRuleMsg::getChatType, 1)
                    .eq(WeQiRuleMsg::getReplyStatus,1)
                    .last("limit 1")
                    .orderByDesc(BaseEntity::getCreateTime));
            if (Objects.nonNull(weQiRuleMsg)) {
                weQiRuleMsgService.update(new LambdaUpdateWrapper<WeQiRuleMsg>()
                        .set(WeQiRuleMsg::getReplyTime, sendTime)
                        .set(WeQiRuleMsg::getReplyMsgId, msgId)
                        .set(WeQiRuleMsg::getReplyStatus, 2)
                        .eq(WeQiRuleMsg::getId, weQiRuleMsg.getId()));
            }
        }
    }

    //外部联系群聊会话处理
    private void externalGroupChatMsgHandler(String msgId, String fromId, String roomId, Date sendTime) {
        WeGroup weGroup = weGroupService.getOne(new LambdaQueryWrapper<WeGroup>()
                .eq(WeGroup::getChatId, roomId)
                .eq(WeGroup::getDelFlag, 0));
        if (Objects.nonNull(weGroup)) {
            //发起为是否为客户
            if (isGroupExternal(fromId, roomId)) {
                //匹配规则
                WeQiRuleListQuery ruleListQuery = new WeQiRuleListQuery();
                ruleListQuery.setChatType(Lists.newArrayList(1, 3));
                ruleListQuery.setUserIds(weGroup.getOwner());
                ruleListQuery.setWorkCycle(DateUtil.dayOfWeek(new Date()));
                ruleListQuery.setFormatTime(DateUtil.date());
                List<WeQiRuleListVo> qiRuleList = weQiRuleService.getQiRuleListByUserId(ruleListQuery);
                if (CollectionUtil.isNotEmpty(qiRuleList)) {
                    WeQiRuleListVo weQiRuleVo = qiRuleList.get(0);
                    Integer timeOut = weQiRuleVo.getTimeOut();

                    WeQiRuleMsg weQiRuleMsg = weQiRuleMsgService.getOne(new LambdaQueryWrapper<WeQiRuleMsg>()
                            .eq(WeQiRuleMsg::getFromId, fromId)
                            .eq(WeQiRuleMsg::getReceiveId, weGroup.getOwner())
                            .eq(WeQiRuleMsg::getChatType, 2)
                            .eq(WeQiRuleMsg::getReplyStatus,1)
                            .last("limit 1")
                            .orderByDesc(BaseEntity::getCreateTime));

                    WeQiRuleMsg ruleMsg = new WeQiRuleMsg();
                    ruleMsg.setRuleId(weQiRuleVo.getId());
                    ruleMsg.setMsgId(msgId);
                    ruleMsg.setTimeOut(DateUtil.offsetMinute(sendTime,timeOut));
                    ruleMsg.setChatType(2);
                    ruleMsg.setFromId(fromId);
                    ruleMsg.setReceiveId(weGroup.getOwner());
                    ruleMsg.setRoomId(roomId);
                    ruleMsg.setSendTime(sendTime);
                    if (Objects.isNull(weQiRuleMsg)) {
                        weQiRuleMsgService.save(ruleMsg);
                    } else {
                        Long between = DateUtil.between(weQiRuleMsg.getSendTime(), DateUtil.date(), DateUnit.MINUTE, true);
                        if (between > timeOut) {
                            weQiRuleMsgService.save(ruleMsg);
                        } else {
                            ruleMsg.setId(weQiRuleMsg.getId());
                            weQiRuleMsgService.updateById(ruleMsg);
                        }
                    }
                }

            } else {
                WeQiRuleMsg weQiRuleMsg = weQiRuleMsgService.getOne(new LambdaQueryWrapper<WeQiRuleMsg>()
                        .eq(WeQiRuleMsg::getRoomId, roomId)
                        .eq(WeQiRuleMsg::getReceiveId, fromId)
                        .eq(WeQiRuleMsg::getChatType, 2)
                        .eq(WeQiRuleMsg::getReplyStatus,1)
                        .last("limit 1")
                        .orderByDesc(BaseEntity::getCreateTime));
                if (Objects.nonNull(weQiRuleMsg)) {
                    weQiRuleMsgService.update(new LambdaUpdateWrapper<WeQiRuleMsg>()
                            .set(WeQiRuleMsg::getReplyTime, sendTime)
                            .set(WeQiRuleMsg::getReplyMsgId, msgId)
                            .set(WeQiRuleMsg::getReplyStatus,2)
                            .eq(WeQiRuleMsg::getId, weQiRuleMsg.getId()));
                }
            }
        }
    }

    //判断是否为客户
    private Boolean isExternal(String externalUserId) {
        int count = weCustomerService.count(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getExternalUserid, externalUserId).eq(WeCustomer::getDelFlag, 0));
        if (count > 0) {
            return true;
        }
        return false;
    }

    //判断是否为群成员
    private Boolean isGroupExternal(String externalUserId, String roomId) {
        int count = weGroupMemberService.count(new LambdaQueryWrapper<WeGroupMember>()
                .eq(WeGroupMember::getChatId, roomId)
                .eq(WeGroupMember::getUserId, externalUserId)
                .eq(WeGroupMember::getType, 2)
                .eq(WeGroupMember::getDelFlag, 0));
        if (count > 0) {
            return true;
        }
        return false;
    }
}
