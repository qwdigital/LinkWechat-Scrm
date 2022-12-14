package com.linkwechat.scheduler.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.WeKfMsgTypeEnum;
import com.linkwechat.common.enums.WeKfStatusEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.*;
import com.linkwechat.domain.kf.query.WeKfMsgTaskQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.service.*;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @Description 客服超时通知处理
 * @date 2021/11/21 19:57
 **/
@Component
@Slf4j
public class QwKfChatMsgKfTimeOutListener {

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

    @Autowired
    private QwAppSendMsgService qwAppSendMsgService;

    @Autowired
    private IWeKfCustomerService weKfCustomerService;

    @Autowired
    private IWeKfNoticeLogService weKfNoticeLogService;

    @Value("${wecom.kf.kf-timeout.notice.content:【微信客服】\r\n 用户【{}】发送咨询消息，你已超过{}未回复。}")
    private String kfTimeoutNoticeContent;

    @RabbitListener(queues = "${wecom.mq.queue.kf-chat-kf-timeout-msg:Qu_KfChatKfTimeOutMsg}")
    @RabbitHandler
    public void kfChatMsgKfTimeOutSubscribe(String msg, Channel channel, Message message) throws IOException {
        try {
            log.info("客服未回复超时消息：msg:{}", msg);
            WeKfMsgTaskQuery query = JSONObject.parseObject(msg, WeKfMsgTaskQuery.class);
            WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(query.getCorpId());
            SecurityContextHolder.setCorpId(weCorpAccount.getCorpId());
            kfChatSessionKfTimeOutHandler(query);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("客服未回复超时消息-消息丢失,重发消息>>>>>>>>>>msg:{}", msg, e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
        } finally {
            SecurityContextHolder.remove();
        }
    }

    private void kfChatSessionKfTimeOutHandler(WeKfMsgTaskQuery query) {
        WeKfPool kfPoolInfo = weKfPoolService.getById(query.getPoolId());
        log.info("-------------处理客服未回复消息 kfPoolInfo:{}", JSONObject.toJSONString(kfPoolInfo));
        if (kfPoolInfo == null || ObjectUtil.equal(kfPoolInfo.getStatus(), WeKfStatusEnum.STAR_OR_END.getType())) {
            return;
        }
        if(StringUtils.isEmpty(kfPoolInfo.getUserId())){
            return;
        }
        WeKfInfo weKfInfo = weKfInfoService.getKfDetailByOpenKfId(query.getCorpId(), query.getOpenKfId());
        log.info("-------------处理客服未回复超时消息 weKfInfo:{}", JSONObject.toJSONString(weKfInfo));
        if (weKfInfo == null) {
            return;
        }
        Integer timeOutNotice = weKfInfo.getKfTimeOutNotice();
        Integer timeOutType = weKfInfo.getKfTimeOutType();
        Integer timeOut = weKfInfo.getKfTimeOut();
        String sendTime = query.getSendTime();
        if (ObjectUtil.equal(1, timeOutNotice)) {
            DateTime offset = null;
            if (ObjectUtil.equal(1, timeOutType)) {
                offset = DateUtil.offset(new Date(), DateField.MINUTE, -timeOut);
            } else {
                offset = DateUtil.offset(new Date(), DateField.HOUR_OF_DAY, -timeOut);
            }

            List<WeKfMsg> lastCustomerMsgList = weKfMsgService.list(new LambdaQueryWrapper<WeKfMsg>()
                    .eq(WeKfMsg::getCorpId, kfPoolInfo.getCorpId())
                    .eq(WeKfMsg::getOpenKfId, kfPoolInfo.getOpenKfId())
                    .eq(WeKfMsg::getExternalUserid, kfPoolInfo.getExternalUserId())
                    .gt(WeKfMsg::getSendTime,sendTime));

            if (CollectionUtil.isEmpty(lastCustomerMsgList)) {
                sendKfNotice(weKfInfo, kfPoolInfo, sendTime);
                //延长超时时间
                extracted(query, timeOutType, timeOut, sendTime);
            }
        }
    }

    private void sendKfNotice(WeKfInfo weKfInfo, WeKfPool kfPoolInfo, String lastCustomerMsg) {
        Integer timeOutType = weKfInfo.getKfTimeOutType();
        Integer timeOut = weKfInfo.getKfTimeOut();
        long betweenMs = DateUtil.betweenMs(DateUtil.parseDateTime(lastCustomerMsg), new Date());
        String timeStr = setLong2TimeOutDuration(betweenMs/1000);
        WeKfCustomer customerInfo = weKfCustomerService.getCustomerInfo(kfPoolInfo.getCorpId(), kfPoolInfo.getExternalUserId());
        WeKfNoticeLog log = new WeKfNoticeLog();
        log.setCorpId(kfPoolInfo.getCorpId());
        log.setExternalUserId(kfPoolInfo.getExternalUserId());
        log.setUserId(kfPoolInfo.getUserId());
        log.setOpenKfId(kfPoolInfo.getOpenKfId());
        log.setSendTime(DateUtil.parseDateTime(lastCustomerMsg));
        if (weKfNoticeLogService.save(log)) {
            //发送应用消息提醒
            QwAppMsgBody qwAppMsgBody = new QwAppMsgBody();
            qwAppMsgBody.setCallBackId(log.getId());
            qwAppMsgBody.setCallBackService("weKfNoticeLogServiceImpl");
            //指定企业
            qwAppMsgBody.setCorpId(weKfInfo.getCorpId());
            //发送人指定员工
            qwAppMsgBody.setCorpUserIds(Collections.singletonList(kfPoolInfo.getUserId()));
            WeMessageTemplate weMessageTemplate = new WeMessageTemplate();
            weMessageTemplate.setMsgType(WeKfMsgTypeEnum.TEXT.getType());
            weMessageTemplate.setContent(StringUtils.format(kfTimeoutNoticeContent, customerInfo.getNickName(), timeStr, kfPoolInfo.getOpenKfId(), kfPoolInfo.getExternalUserId()));
            qwAppMsgBody.setMessageTemplates(weMessageTemplate);
            qwAppSendMsgService.appMsgSend(qwAppMsgBody);
        }
    }

    private String setLong2TimeOutDuration(Long outTimeDuration) {
        String timeOutDuration = "";
        long hours = outTimeDuration / 3600;            //转换小时
        outTimeDuration = outTimeDuration % 3600;                //剩余秒数
        long minutes = outTimeDuration / 60;            //转换分钟
        if (hours > 0) {
            timeOutDuration = hours + "小时" + minutes + "分钟";
        } else {
            timeOutDuration = minutes + "分钟";
        }
        return timeOutDuration;
    }

    private void extracted(WeKfMsgTaskQuery query, Integer timeOutType, Integer timeOut, String sendTime) {
        int count = weKfNoticeLogService.count(new LambdaQueryWrapper<WeKfNoticeLog>()
                .eq(WeKfNoticeLog::getCorpId, query.getCorpId())
                .eq(WeKfNoticeLog::getOpenKfId, query.getOpenKfId())
                .eq(WeKfNoticeLog::getExternalUserId, query.getExternalUserId())
                .ge(BaseEntity::getCreateTime, sendTime));
        //时间叠加
        if(count >= 0 && count < 3){
            timeOut = (1 + count) * timeOut;
        }
        if (count >= 3 && ObjectUtil.equal(1, timeOutType)) {
            timeOutType = 2;
        }
        //延长超时时间直到会话结束
        long diffTime = 0L;
        if (ObjectUtil.equal(1, timeOutType)) {
            diffTime = timeOut * 60 * 1000 ;
        } else {
            diffTime = timeOut * 60 * 60 * 1000;
        }
        long finalDiffTime = diffTime;
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeKfChatMsgDelayEx(), rabbitMQSettingConfig.getWeKfChatKfTimeOutMsgRk(), JSONObject.toJSONString(query), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", finalDiffTime);
            return message;
        });
    }

}
