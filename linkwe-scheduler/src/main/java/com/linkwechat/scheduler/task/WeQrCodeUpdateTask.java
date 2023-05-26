package com.linkwechat.scheduler.task;

import com.linkwechat.service.IWeQrCodeService;
import com.rabbitmq.client.Channel;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 员工活码变更任务
 * @date 2021/2/24 0:41
 **/
@Slf4j
@Component
public class WeQrCodeUpdateTask {

    @Autowired
    private IWeQrCodeService weQrCodeService;

    @XxlJob("weQrCodeUpdateTask")
    public void process() {
        String params = XxlJobHelper.getJobParam();
        log.info("员工活码变更任务>>>>>>>>>>>>>>>>>>>启动 params:{}", params);
        weQrCodeService.qrCodeUpdateTask(null);
    }


    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.qr-code-change:Qu_QrCodeChange}")
    public void subscribe(String qrId, Channel channel, Message message) {
        try {
            log.info("员工活码变更通知：qrId:{}",qrId);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            weQrCodeService.qrCodeUpdateTask(Long.valueOf(qrId));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("员工活码变更通知-消息处理失败 qrId:{},error:{}",qrId,e);
        }
    }
}
