package com.linkwechat.scheduler.listener;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.WeShortLinkPromotion;
import com.linkwechat.domain.WeShortLinkUserPromotionTask;
import com.linkwechat.domain.moments.dto.CancelMomentTaskDto;
import com.linkwechat.domain.moments.dto.MomentsCreateResultDto;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionTaskEndQuery;
import com.linkwechat.domain.wecom.query.customer.msg.WeCancelGroupMsgSendQuery;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.fegin.QwMomentsClient;
import com.linkwechat.service.IWeShortLinkPromotionService;
import com.linkwechat.service.IWeShortLinkUserPromotionTaskService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private IWeShortLinkPromotionService weShortLinkPromotionService;
    @Resource
    private IWeShortLinkUserPromotionTaskService weShortLinkUserPromotionTaskService;
    @Resource
    private QwCustomerClient qwCustomerClient;
    @Resource
    private QwMomentsClient qwMomentsClient;

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
    @Transactional(rollbackFor = Exception.class)
    public void handler(WeShortLinkPromotionTaskEndQuery query) {
        Integer type = query.getType();
        switch (type) {
            case 0:
                clientHandler(query.getPromotionId(), query.getBusinessId());
                break;
            case 1:
                groupHandler(query.getPromotionId(), query.getBusinessId());
                break;
            case 2:
                momentsHandler(query.getPromotionId(), query.getBusinessId());
                break;
            case 3:
                appMsgHandler(query.getPromotionId(), query.getBusinessId());
                break;
            default:
                break;
        }
    }

    /**
     * 群发客户处理
     *
     * @param promotionId
     * @param businessId
     */
    private void clientHandler(Long promotionId, Long businessId) {
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

        //更新短链推广状态为已结束
        LambdaUpdateWrapper<WeShortLinkPromotion> promotionUpdateWrapper = Wrappers.lambdaUpdate();
        promotionUpdateWrapper.eq(WeShortLinkPromotion::getId, promotionId);
        promotionUpdateWrapper.set(WeShortLinkPromotion::getTaskStatus, 2);
        weShortLinkPromotionService.update(promotionUpdateWrapper);
    }

    /**
     * 群发客群处理
     *
     * @param promotionId
     * @param businessId
     */
    private void groupHandler(Long promotionId, Long businessId) {
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

        //更新短链推广状态为已结束
        LambdaUpdateWrapper<WeShortLinkPromotion> promotionUpdateWrapper = Wrappers.lambdaUpdate();
        promotionUpdateWrapper.eq(WeShortLinkPromotion::getId, promotionId);
        promotionUpdateWrapper.set(WeShortLinkPromotion::getTaskStatus, 2);
        weShortLinkPromotionService.update(promotionUpdateWrapper);
    }

    /**
     * 朋友圈处理
     *
     * @param promotionId
     * @param businessId
     */
    private void momentsHandler(Long promotionId, Long businessId) {
        LambdaQueryWrapper<WeShortLinkUserPromotionTask> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeShortLinkUserPromotionTask::getTemplateType, 2);
        queryWrapper.eq(WeShortLinkUserPromotionTask::getTemplateId, businessId);
        queryWrapper.eq(WeShortLinkUserPromotionTask::getDelFlag, 0);
        WeShortLinkUserPromotionTask one = weShortLinkUserPromotionTaskService.getOne(queryWrapper);
        Optional.ofNullable(one).ifPresent(i -> {
            AjaxResult<MomentsCreateResultDto> momentTaskResult = qwMomentsClient.getMomentTaskResult(i.getMsgId());
            Optional.ofNullable(momentTaskResult).filter(o -> o.getCode() == 200).ifPresent(m -> {
                MomentsCreateResultDto data = m.getData();
                MomentsCreateResultDto.Result result = data.getResult();
                //停止发送朋友圈
                CancelMomentTaskDto cancelMomentTaskDto = new CancelMomentTaskDto();
                cancelMomentTaskDto.setMoment_id(result.getMoment_id());
                qwMomentsClient.cancel_moment_task(cancelMomentTaskDto);

                //更新员工短链任务推广
                LambdaUpdateWrapper<WeShortLinkUserPromotionTask> updateWrapper = Wrappers.lambdaUpdate();
                updateWrapper.eq(WeShortLinkUserPromotionTask::getId, one.getId());
                updateWrapper.set(WeShortLinkUserPromotionTask::getSendStatus, 3);
                updateWrapper.set(WeShortLinkUserPromotionTask::getMomentId, result.getMoment_id());
                weShortLinkUserPromotionTaskService.update(updateWrapper);

                //更新短链推广状态为已结束
                LambdaUpdateWrapper<WeShortLinkPromotion> promotionUpdateWrapper = Wrappers.lambdaUpdate();
                promotionUpdateWrapper.eq(WeShortLinkPromotion::getId, promotionId);
                promotionUpdateWrapper.set(WeShortLinkPromotion::getTaskStatus, 2);
                weShortLinkPromotionService.update(promotionUpdateWrapper);
            });
        });
    }


    /**
     * 应用消息处理
     *
     * @param promotionId
     * @param businessId
     */
    public void appMsgHandler(Long promotionId, Long businessId) {
        //更新短链推广状态为已结束
        LambdaUpdateWrapper<WeShortLinkPromotion> promotionUpdateWrapper = Wrappers.lambdaUpdate();
        promotionUpdateWrapper.eq(WeShortLinkPromotion::getId, promotionId);
        promotionUpdateWrapper.set(WeShortLinkPromotion::getTaskStatus, 2);
        weShortLinkPromotionService.update(promotionUpdateWrapper);
    }

}
