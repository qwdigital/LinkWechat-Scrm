package com.linkwechat.scheduler.listener;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.enums.QwAppMsgBusinessTypeEnum;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.WeShortLinkPromotion;
import com.linkwechat.domain.WeShortLinkPromotionTemplateAppMsg;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.domain.shortlink.dto.WeShortLinkPromotionAppMsgDto;
import com.linkwechat.scheduler.service.AbstractAppMsgService;
import com.linkwechat.service.IWeShortLinkPromotionService;
import com.linkwechat.service.IWeShortLinkPromotionTemplateAppMsgService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

/**
 * @author danmo
 * @description 企微应用消息监听
 * @date 2022/4/3 15:39
 **/
@Slf4j
@Component
public class QwAppMsgListener {

    @Resource
    private IWeShortLinkPromotionService weShortLinkPromotionService;
    @Resource
    private IWeShortLinkPromotionTemplateAppMsgService weShortLinkPromotionTemplateAppMsgService;


    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.app-msg:Qu_AppMsg}")
    public void subscribe(String msg, Channel channel, Message message) throws IOException {
        try {
            log.info("应用通知消息监听：msg:{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            QwAppMsgBody appMsgBody = JSONObject.parseObject(msg, QwAppMsgBody.class);
            QwAppMsgBusinessTypeEnum qwAppMsgBusinessTypeEnum = QwAppMsgBusinessTypeEnum.parseEnum(appMsgBody.getBusinessType());
            if (Objects.nonNull(qwAppMsgBusinessTypeEnum)) {
                switch (qwAppMsgBusinessTypeEnum) {
                    case AGENT:
                        SpringUtils.getBean(qwAppMsgBusinessTypeEnum.getBeanName(), AbstractAppMsgService.class).sendAgentMsg(appMsgBody);
                        break;
                    case QI_RULE:
                        SpringUtils.getBean(qwAppMsgBusinessTypeEnum.getBeanName(), AbstractAppMsgService.class).sendAppMsg(appMsgBody);
                        break;
                    default: //默认通用发送
                        SpringUtils.getBean(qwAppMsgBusinessTypeEnum.getBeanName(), AbstractAppMsgService.class).sendAppMsg(appMsgBody);
                        break;
                }
            }
        } catch (Exception e) {
            log.error("应用通知消息监听-消息处理失败 msg:{},error:{}", msg, e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
        }
    }


    /**
     * 短链推广 - 企微延迟应用消息监听
     *
     * @param msg
     * @param channel
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.delay_app-msg:Qu_Delay_AppMsg}")
    public void delaySubscribe(String msg, Channel channel, Message message) {
        try {
            log.info("企微延迟应用消息监听：msg:{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            WeShortLinkPromotionAppMsgDto appMsgBody = JSONObject.parseObject(msg, WeShortLinkPromotionAppMsgDto.class);

            //1.判断任务是否处于待推广的状态
            WeShortLinkPromotion weShortLinkPromotion = weShortLinkPromotionService.getById(appMsgBody.getShortLinkPromotionId());

            if (BeanUtil.isEmpty(weShortLinkPromotion)) {
                return;
            } else {
                //判断任务的推广状态和删除状态
                //删除标识 0 正常 1 删除
                //任务状态: 0待推广 1推广中 2已结束
                if (!(weShortLinkPromotion.getDelFlag().equals(0) && weShortLinkPromotion.getTaskStatus().equals(0))) {
                    return;
                }
            }

            //查看短链推广模板是否已经删除
            Long businessId = appMsgBody.getBusinessId();
            if (weShortLinkPromotion.getType().equals(3)) {
                WeShortLinkPromotionTemplateAppMsg appMsg = weShortLinkPromotionTemplateAppMsgService.getById(businessId);
                if (appMsg == null || appMsg.getDelFlag().equals(1)) {
                    //删除直接跳出，不继续执行。
                    return;
                }
            }

            QwAppMsgBusinessTypeEnum qwAppMsgBusinessTypeEnum = QwAppMsgBusinessTypeEnum.parseEnum(appMsgBody.getBusinessType());
            if (Objects.nonNull(qwAppMsgBusinessTypeEnum)) {
                switch (qwAppMsgBusinessTypeEnum) {
                    case AGENT:
                        SpringUtils.getBean(qwAppMsgBusinessTypeEnum.getBeanName(), AbstractAppMsgService.class).sendAgentMsg(appMsgBody);
                        break;
                    default: //默认通用发送
                        SpringUtils.getBean(qwAppMsgBusinessTypeEnum.getBeanName(), AbstractAppMsgService.class).sendAppMsg(appMsgBody);
                        //修改状态为推广中
                        LambdaUpdateWrapper<WeShortLinkPromotion> promotionUpdate = Wrappers.lambdaUpdate();
                        promotionUpdate.set(WeShortLinkPromotion::getTaskStatus, 1);
                        promotionUpdate.eq(WeShortLinkPromotion::getId, weShortLinkPromotion.getId());
                        weShortLinkPromotionService.update(promotionUpdate);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("企微延迟应用消息监听-消息处理失败 msg:{},error:{}", msg, e);
        }
    }


}
