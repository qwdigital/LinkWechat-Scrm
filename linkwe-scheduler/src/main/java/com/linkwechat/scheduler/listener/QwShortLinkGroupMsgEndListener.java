package com.linkwechat.scheduler.listener;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.domain.WeShortLinkUserPromotionTask;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionTaskEndQuery;
import com.linkwechat.domain.wecom.query.customer.msg.WeCancelGroupMsgSendQuery;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.service.IWeShortLinkUserPromotionTaskService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 短链推广群发任务结束-监听
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/14 18:01
 */
@Slf4j
@Component
public class QwShortLinkGroupMsgEndListener {
    @Resource
    private IWeShortLinkUserPromotionTaskService weShortLinkUserPromotionTaskService;
    @Resource
    private QwCustomerClient qwCustomerClient;

    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.delay-group-msg-end:Qu_DelayGroupMsgEnd}")
    public void subscribe(String msg, Channel channel, Message message) {
        try {
            log.info("短链推广群发任务结束监听：msg:{}", msg);
            WeShortLinkPromotionTaskEndQuery query = JSONObject.parseObject(msg, WeShortLinkPromotionTaskEndQuery.class);
            handler(query);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("应用通知消息监听-消息处理失败 msg:{},error:{}", msg, e);
        }
    }

    /**
     * 数据处理
     *
     * @author WangYX
     * @date 2023/03/14 18:18
     * @version 1.0.0
     */
    public void handler(WeShortLinkPromotionTaskEndQuery query) {
        Integer type = query.getType();
        switch (type) {
            case 0:
                clientHandler(query.getBusinessId());
                break;
            case 1:
                groupHandler(query.getBusinessId());
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }

    /**
     * 群发客户处理
     *
     * @param businessId
     */
    private void clientHandler(Long businessId) {
        LambdaQueryWrapper<WeShortLinkUserPromotionTask> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeShortLinkUserPromotionTask::getTemplateType, 0);
        queryWrapper.eq(WeShortLinkUserPromotionTask::getTemplateId, businessId);
        queryWrapper.eq(WeShortLinkUserPromotionTask::getDelFlag, 0);
        List<WeShortLinkUserPromotionTask> list = weShortLinkUserPromotionTaskService.list(queryWrapper);
        if (list != null && list.size() > 0) {
            list.stream().forEach(i -> Optional.ofNullable(i.getMsgId()).ifPresent(o -> {
                WeCancelGroupMsgSendQuery query = new WeCancelGroupMsgSendQuery();
                query.setMsgid(o);
                qwCustomerClient.cancelGroupMsgSend(query);
            }));
        }
    }

    /**
     * 群发客群处理
     *
     * @param businessId
     */
    private void groupHandler(Long businessId) {
        LambdaQueryWrapper<WeShortLinkUserPromotionTask> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeShortLinkUserPromotionTask::getTemplateType, 1);
        queryWrapper.eq(WeShortLinkUserPromotionTask::getTemplateId, businessId);
        queryWrapper.eq(WeShortLinkUserPromotionTask::getDelFlag, 0);
        List<WeShortLinkUserPromotionTask> list = weShortLinkUserPromotionTaskService.list(queryWrapper);
        if (list != null && list.size() > 0) {
            list.stream().forEach(i -> Optional.ofNullable(i.getMsgId()).ifPresent(o -> {
                WeCancelGroupMsgSendQuery query = new WeCancelGroupMsgSendQuery();
                query.setMsgid(o);
                qwCustomerClient.cancelGroupMsgSend(query);
            }));
        }
    }


}
