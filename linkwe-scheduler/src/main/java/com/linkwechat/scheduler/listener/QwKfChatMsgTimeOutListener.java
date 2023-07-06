package com.linkwechat.scheduler.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.WeKfMsgTypeEnum;
import com.linkwechat.common.enums.WeKfOriginEnum;
import com.linkwechat.common.enums.WeKfStatusEnum;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WeKfInfo;
import com.linkwechat.domain.WeKfMsg;
import com.linkwechat.domain.WeKfPool;
import com.linkwechat.domain.kf.query.WeKfMsgTaskQuery;
import com.linkwechat.domain.wecom.query.kf.WeKfMsgQuery;
import com.linkwechat.domain.wecom.vo.kf.WeKfMsgVo;
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
import java.util.List;
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

    @Resource
    private QwKfClient qwKfClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @RabbitListener(queues = "${wecom.mq.queue.kf-chat-timeout-msg:Qu_KfChatTimeOutMsg}")
    @RabbitHandler
    public void kfChatMsgTimeOutSubscribe(String msg, Channel channel, Message message) throws IOException {
        try {
            log.info("客户未回复超时消息：msg:{}", msg);
            WeKfMsgTaskQuery query = JSONObject.parseObject(msg, WeKfMsgTaskQuery.class);
            WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(query.getCorpId());
            SecurityContextHolder.setCorpId(weCorpAccount.getCorpId());
            kfChatSessionTimeOutHandler(query);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("客户未回复超时消息-消息丢失,重发消息>>>>>>>>>>msg:{}", msg, e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
        } finally {
            SecurityContextHolder.remove();
        }
    }

    private void kfChatSessionTimeOutHandler(WeKfMsgTaskQuery query) {
        WeKfPool kfPoolInfo = weKfPoolService.getById(query.getPoolId());
        log.info("-------------处理客户未回复超时消息 kfPoolInfo:{}", JSONObject.toJSONString(kfPoolInfo));
        if (kfPoolInfo == null || ObjectUtil.equal(kfPoolInfo.getStatus(), WeKfStatusEnum.STAR_OR_END.getType())) {
            return;
        }
        WeKfInfo weKfInfo = weKfInfoService.getKfDetailByOpenKfId(query.getCorpId(), query.getOpenKfId());
        log.info("-------------处理客户未回复超时消息 weKfInfo:{}", JSONObject.toJSONString(weKfInfo));
        if (weKfInfo == null) {
            return;
        }
        Integer timeOutNotice = weKfInfo.getTimeOutNotice();
        Integer timeOutType = weKfInfo.getTimeOutType();
        Integer timeOut = weKfInfo.getTimeOut();
        String sendTime = query.getSendTime();
        if (ObjectUtil.equal(1, timeOutNotice)) {
            DateTime offset = null;
            if (ObjectUtil.equal(1, timeOutType)) {
                offset = DateUtil.offset(new Date(), DateField.MINUTE, -timeOut);
            } else {
                offset = DateUtil.offset(new Date(), DateField.HOUR_OF_DAY, -timeOut);
            }

            List<WeKfMsg> kfMsgList = weKfMsgService.list(new LambdaQueryWrapper<WeKfMsg>()
                    .eq(WeKfMsg::getCorpId, kfPoolInfo.getCorpId())
                    .eq(WeKfMsg::getOpenKfId, kfPoolInfo.getOpenKfId())
                    .eq(WeKfMsg::getExternalUserid, kfPoolInfo.getExternalUserId())
                    .gt(WeKfMsg::getSendTime, sendTime));

            if (CollectionUtil.isEmpty(kfMsgList)) {
                sendCutomerTimeOutNotice(query, weKfInfo, kfPoolInfo);
            }

        }
    }

    private void sendCutomerTimeOutNotice(WeKfMsgTaskQuery query, WeKfInfo weKfInfo, WeKfPool kfPoolInfo) {
        //发送超时会话
        String timeOutContent = weKfInfo.getTimeOutContent();
        WeKfMsgQuery weKfMsgQuery = new WeKfMsgQuery();
        JSONObject msg = new JSONObject();
        msg.put("content", timeOutContent);
        weKfMsgQuery.setMsgtype(WeKfMsgTypeEnum.TEXT.getType());
        weKfMsgQuery.setCode(kfPoolInfo.getMsgCode());
        weKfMsgQuery.setContext(msg);
        WeKfMsgVo weKfMsgVo = qwKfClient.sendMsgOnEvent(weKfMsgQuery).getData();
        if (Objects.nonNull(weKfMsgVo) && StringUtils.isNotEmpty(weKfMsgVo.getMsgId())) {
            WeKfMsg weKfMsg = new WeKfMsg();
            weKfMsg.setMsgId(weKfMsgVo.getMsgId());
            weKfMsg.setMsgType(weKfMsgQuery.getMsgtype());
            weKfMsg.setOpenKfId(kfPoolInfo.getOpenKfId());
            weKfMsg.setExternalUserid(kfPoolInfo.getExternalUserId());
            weKfMsg.setContent(weKfMsgQuery.getContext().toJSONString());
            weKfMsg.setOrigin(WeKfOriginEnum.SERVICER_SEND.getType());
            weKfMsg.setCorpId(SecurityUtils.getCorpId());
            weKfMsg.setSendTime(DateUtil.parseDateTime(DateUtil.now()));
            if(weKfMsgService.save(weKfMsg)){
                query.setSendTime(DateUtil.formatDateTime(weKfMsg.getSendTime()));
                rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeKfChatMsgDelayEx(), rabbitMQSettingConfig.getWeKfChatEndMsgRk(), JSONObject.toJSONString(query), message -> {
                    Integer endNoticeTime = weKfInfo.getEndNoticeTime();
                    long offset = 0L;
                    if (ObjectUtil.equal(1, weKfInfo.getEndTimeType())) {
                        offset = endNoticeTime * 60 * 1000;
                    } else {
                        offset = endNoticeTime * 60 * 60 * 1000;
                    }
                    //注意这里时间可使用long类型,毫秒单位，设置header
                    message.getMessageProperties().setHeader("x-delay", offset);
                    return message;
                });
            }
        }
    }

}
