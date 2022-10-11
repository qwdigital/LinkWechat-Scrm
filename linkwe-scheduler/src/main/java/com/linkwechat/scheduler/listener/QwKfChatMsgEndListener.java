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

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * @author danmo
 * @Description 客服结束会话处理
 * @date 2021/11/21 19:57
 **/
@Component
@Slf4j
public class QwKfChatMsgEndListener {

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IWeKfMsgService weKfMsgService;
    @Autowired
    private IWeKfInfoService weKfInfoService;
    @Autowired
    private IWeKfPoolService weKfPoolService;

    @Resource
    private QwKfClient qwKfClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;


    @RabbitListener(queues = "${wecom.mq.queue.kf-chat-end-msg:Qu_KfChatEndMsg}")
    @RabbitHandler
    public void kfChatMsgTimeOutSubscribe(String msg, Channel channel, Message message) throws IOException {
        try {
            log.info("客服结束会话消息：msg:{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            WeKfMsgTaskQuery query = JSONObject.parseObject(msg, WeKfMsgTaskQuery.class);
            kfChatSessionEndHandler(query);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("客服结束会话消息-消息丢失,重发消息>>>>>>>>>>msg:{}", msg);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
        } finally {
            SecurityContextHolder.remove();
        }
    }

    private void kfChatSessionEndHandler(WeKfMsgTaskQuery query) {
        WeKfPool kfPoolInfo = weKfPoolService.getKfPoolInfo(query.getCorpId(), query.getOpenKfId(), query.getExternalUserId());
        log.info("-------------处理客服结束消息 kfPoolInfo:{}", JSONObject.toJSONString(kfPoolInfo));
        if (kfPoolInfo == null
                || ObjectUtil.equal(kfPoolInfo.getStatus(), WeKfStatusEnum.STAR_OR_END.getType())) {
            return;
        }

        WeKfInfoVo weKfInfo = weKfInfoService.getKfInfoByOpenKfId(query.getCorpId(), query.getOpenKfId());
        log.info("-------------处理客服结束消息 weKfInfo：{}", JSONObject.toJSONString(weKfInfo));
        if (weKfInfo == null) {
            return;
        }

        Integer endNotice = weKfInfo.getEndNotice();
        Integer endTimeType = weKfInfo.getEndTimeType();
        Integer endNoticeTime = weKfInfo.getEndNoticeTime();

        String sendTime = DateUtil.date(query.getSendTime()).toString();

        if (ObjectUtil.equal(1, endNotice)) {
            DateTime offset = null;
            if (ObjectUtil.equal(1, endTimeType)) {
                offset = DateUtil.offset(new Date(), DateField.MINUTE, -endNoticeTime);
            } else {
                offset = DateUtil.offset(new Date(), DateField.HOUR_OF_DAY, -endNoticeTime);
            }

            WeKfMsg lastCustomerMsg = weKfMsgService.getLastCustomerMsg(query.getCorpId(), query.getOpenKfId(), query.getExternalUserId(), sendTime);
            if (Objects.isNull(lastCustomerMsg)) {
                return;
            }
            if (offset.isBeforeOrEquals(lastCustomerMsg.getSendTime())) {
                //延长结束时间
                extracted(query, endTimeType, endNoticeTime, lastCustomerMsg);
            } else if (ObjectUtil.equal(lastCustomerMsg.getOrigin(), 5) && offset.isBeforeOrEquals(lastCustomerMsg.getSendTime())) {
                //结束会话
                weKfPoolService.allocationServicer(query.getCorpId(), query.getOpenKfId(), query.getExternalUserId(), null, 4);
                String endCode = redisService.getCacheObject(StringUtils.format(WeConstans.KF_SESSION_MSG_CODE_KEY, query.getCorpId(), query.getOpenKfId(), query.getExternalUserId()));
                if (StringUtils.isNotEmpty(endCode)) {
                    WeKfMsgQuery weKfMsgQuery = new WeKfMsgQuery();
                    JSONObject msg = new JSONObject();
                    msg.put("content", weKfInfo.getEndContent());
                    weKfMsgQuery.setMsgtype(WeKfMsgTypeEnum.TEXT.getType());
                    weKfMsgQuery.setCode(endCode);
                    weKfMsgQuery.setContext(msg);
                    qwKfClient.sendMsgOnEvent(weKfMsgQuery);
                    //通知接待池处理
                    weKfPoolService.transferReceptionPoolCustomer(query.getCorpId(), query.getOpenKfId(), query.getExternalUserId());
                }
            }
        }
    }

    private void extracted(WeKfMsgTaskQuery query, Integer endTimeType, Integer endNoticeTime, WeKfMsg lastCustomerMsg) {
        Date lastCustomerMsgSendTime = lastCustomerMsg.getSendTime();
        long diffSendTime2NowTime = DateUtil.betweenDay(lastCustomerMsgSendTime, new Date(), false);
        if (diffSendTime2NowTime >= 2) {
            WeKfPool weKfPool = new WeKfPool();
            weKfPool.setCorpId(query.getCorpId());
            weKfPool.setOpenKfId(query.getOpenKfId());
            weKfPool.setExternalUserId(query.getExternalUserId());
            weKfPool.setStatus(WeKfStatusEnum.STAR_OR_END.getType());
            weKfPool.setDelFlag(1);
            weKfPool.setSessionEndTime(new Date());
            weKfPoolService.updateKfPoolInfo(weKfPool);
        }
        //延长结束时间
        DateTime delayTime = null;
        if (ObjectUtil.equal(1, endTimeType)) {
            delayTime = DateUtil.offset(lastCustomerMsg.getSendTime(), DateField.MINUTE, endNoticeTime);
        } else {
            delayTime = DateUtil.offset(lastCustomerMsg.getSendTime(), DateField.HOUR_OF_DAY, endNoticeTime);
        }
        query.setSendTime(delayTime.getTime());
        log.info("-------------处理客服结束消息 延长时间 query：{}", JSONObject.toJSONString(query));
        long diffTime = DateUtils.diffTime(delayTime, new Date());
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeKfChatMsgDelayEx(), rabbitMQSettingConfig.getWeKfChatEndMsgRk(), JSONObject.toJSONString(query), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", diffTime);
            return message;
        });
    }

}
