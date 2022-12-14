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
import com.linkwechat.common.enums.WeKfStatusEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WeKfInfo;
import com.linkwechat.domain.WeKfMsg;
import com.linkwechat.domain.WeKfPool;
import com.linkwechat.domain.kf.query.WeKfMsgTaskQuery;
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
            WeKfMsgTaskQuery query = JSONObject.parseObject(msg, WeKfMsgTaskQuery.class);
            WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(query.getCorpId());
            SecurityContextHolder.setCorpId(weCorpAccount.getCorpId());
            kfChatSessionEndHandler(query);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("客服结束会话消息-消息丢失,重发消息>>>>>>>>>>msg:{}", msg, e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
        } finally {
            SecurityContextHolder.remove();
        }
    }

    private void kfChatSessionEndHandler(WeKfMsgTaskQuery query) {
        WeKfPool kfPoolInfo = weKfPoolService.getById(query.getPoolId());
        log.info("-------------处理客服结束消息 kfPoolInfo:{}", JSONObject.toJSONString(kfPoolInfo));
        if (kfPoolInfo == null
                || ObjectUtil.equal(kfPoolInfo.getStatus(), WeKfStatusEnum.STAR_OR_END.getType())) {
            return;
        }
        WeKfInfo weKfInfo = weKfInfoService.getKfDetailByOpenKfId(query.getCorpId(), query.getOpenKfId());
        log.info("-------------处理客服结束消息 weKfInfo：{}", JSONObject.toJSONString(weKfInfo));
        if (weKfInfo == null) {
            return;
        }

        Integer endNotice = weKfInfo.getEndNotice();
        Integer endTimeType = weKfInfo.getEndTimeType();
        Integer endNoticeTime = weKfInfo.getEndNoticeTime();


        if (ObjectUtil.equal(1, endNotice)) {
            DateTime offset = null;
            if (ObjectUtil.equal(1, endTimeType)) {
                offset = DateUtil.offset(new Date(), DateField.MINUTE, -endNoticeTime);
            } else {
                offset = DateUtil.offset(new Date(), DateField.HOUR_OF_DAY, -endNoticeTime);
            }

            List<WeKfMsg> kfMsgList = weKfMsgService.list(new LambdaQueryWrapper<WeKfMsg>()
                    .eq(WeKfMsg::getCorpId, kfPoolInfo.getCorpId())
                    .eq(WeKfMsg::getOpenKfId, kfPoolInfo.getOpenKfId())
                    .eq(WeKfMsg::getExternalUserid, kfPoolInfo.getExternalUserId())
                    .gt(WeKfMsg::getSendTime, query.getSendTime()));

            if (CollectionUtil.isEmpty(kfMsgList)) {
                //结束会话
                String endCode = weKfPoolService.allocationServicer(query.getCorpId(), query.getOpenKfId(), query.getExternalUserId(), null, 4);
                if (StringUtils.isNotEmpty(endCode)) {
                    //发送结束语
                    weKfInfoService.sendEndMsg(endCode, weKfInfo, kfPoolInfo);
                    //通知接待池处理
                    weKfPoolService.transferReceptionPoolCustomer(query.getCorpId(), query.getOpenKfId(), query.getExternalUserId());
                }
            }
        }
    }
}
