package com.linkwechat.scheduler.listener;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.WeKfMsgTypeEnum;
import com.linkwechat.common.enums.WeKfStatusEnum;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WeKfMsg;
import com.linkwechat.domain.WeKfPool;
import com.linkwechat.domain.kf.query.WeKfMsgTaskQuery;
import com.linkwechat.domain.kf.vo.WeKfInfoVo;
import com.linkwechat.domain.wecom.query.kf.WeKfMsgQuery;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeKfInfoService;
import com.linkwechat.service.IWeKfMsgService;
import com.linkwechat.service.IWeKfPoolService;
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
import java.util.Objects;

/**
 * @author danmo
 * @Description 客服超时通知处理
 * @date 2021/11/21 19:57
 **/
@Component
@Slf4j
public class QwKfChatMsgTimeOutListener {

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private IWeKfMsgService weKfMsgService;
    @Autowired
    private IWeKfInfoService weKfInfoService;

    @Autowired
    private IWeKfPoolService weKfPoolService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private QwKfClient qwKfClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @RabbitListener(queues = "${wecom.mq.queue.kf-chat-timeout-msg:Qu_KfChatTimeOutMsg}")
    @RabbitHandler
    public void kfChatMsgTimeOutSubscribe(String msg, Channel channel, Message message) throws IOException {
        try {
            log.info("客服超时消息：msg:{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            WeKfMsgTaskQuery query = JSONObject.parseObject(msg, WeKfMsgTaskQuery.class);
            kfChatSessionTimeOutHandler(query);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("客服超时消息-消息丢失,重发消息>>>>>>>>>>msg:{}", msg);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
        } finally {
            SecurityContextHolder.remove();
        }
    }

    private void kfChatSessionTimeOutHandler(WeKfMsgTaskQuery query) {
        WeKfPool kfPoolInfo = weKfPoolService.getKfPoolInfo(query.getCorpId(), query.getOpenKfId(), query.getExternalUserId());
        log.info("-------------处理客服超时消息 kfPoolInfo:{}", JSONObject.toJSONString(kfPoolInfo));
        if (kfPoolInfo == null
                || ObjectUtil.equal(kfPoolInfo.getStatus(), WeKfStatusEnum.STAR_OR_END.getType())
                || ObjectUtil.equal(kfPoolInfo.getStatus(), WeKfStatusEnum.ACCESS_POOL.getType())) {
            return;
        }
        WeKfInfoVo weKfInfo = weKfInfoService.getKfInfoByOpenKfId(query.getCorpId(), query.getOpenKfId());
        log.info("-------------处理客服超时消息 weKfInfo:{}", JSONObject.toJSONString(weKfInfo));
        if (weKfInfo == null) {
            return;
        }
        Integer timeOutNotice = weKfInfo.getTimeOutNotice();
        Integer timeOutType = weKfInfo.getTimeOutType();
        Integer timeOut = weKfInfo.getTimeOut();
        String sendTime = DateUtil.date(query.getSendTime()).toString();
        if (ObjectUtil.equal(1, timeOutNotice)) {
            DateTime offset = null;
            if (ObjectUtil.equal(1, timeOutType)) {
                offset = DateUtil.offset(new Date(), DateField.MINUTE, -timeOut);
            } else {
                offset = DateUtil.offset(new Date(), DateField.HOUR_OF_DAY, -timeOut);
            }

            WeKfMsg lastCustomerMsg = weKfMsgService.getLastCustomerMsg(query.getCorpId(), query.getOpenKfId(), query.getExternalUserId(), sendTime);
            if (Objects.isNull(lastCustomerMsg)) {
                return;
            }
            //如果最后一条会话为客服会话并且定时时间超过客服会话时间则发送超时提示
            if (ObjectUtil.equal(lastCustomerMsg.getOrigin(), 5) && offset.isAfterOrEquals(lastCustomerMsg.getSendTime())) {
                String msgCode = redisService.getCacheObject(StringUtils.format(WeConstans.KF_SESSION_MSG_CODE_KEY, query.getCorpId(),
                        query.getOpenKfId(), query.getExternalUserId()));
                //发送超时会话
                String timeOutContent = weKfInfo.getTimeOutContent();
                WeKfMsgQuery weKfMsgQuery = new WeKfMsgQuery();
                JSONObject msg = new JSONObject();
                msg.put("content", timeOutContent);
                weKfMsgQuery.setMsgtype(WeKfMsgTypeEnum.TEXT.getType());
                weKfMsgQuery.setCode(msgCode);
                weKfMsgQuery.setContext(msg);
                qwKfClient.sendMsgOnEvent(weKfMsgQuery);
            } else if (offset.isBeforeOrEquals(lastCustomerMsg.getSendTime())) {
                //延长超时时间
                extracted(query, timeOutType, timeOut, lastCustomerMsg);
            }
        }
    }

    private void extracted(WeKfMsgTaskQuery query, Integer timeOutType, Integer timeOut, WeKfMsg lastCustomerMsg) {
        Date lastCustomerMsgSendTime = lastCustomerMsg.getSendTime();
        long diffSendTime2NowTime = DateUtil.betweenDay(lastCustomerMsgSendTime, new Date(), false);
        if (diffSendTime2NowTime >= 2) {
            return;
        }
        //延长超时时间直到会话结束
        DateTime delayTime = null;
        if (ObjectUtil.equal(1, timeOutType)) {
            delayTime = DateUtil.offset(new Date(), DateField.MINUTE, timeOut);
        } else {
            delayTime = DateUtil.offset(new Date(), DateField.HOUR_OF_DAY, timeOut);
        }
        query.setSendTime(delayTime.getTime());
        long diffTime = DateUtils.diffTime(delayTime, new Date());
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeKfChatMsgDelayEx(), rabbitMQSettingConfig.getWeKfChatTimeOutMsgRk(), JSONObject.toJSONString(query), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", diffTime);
            return message;
        });
    }

}
