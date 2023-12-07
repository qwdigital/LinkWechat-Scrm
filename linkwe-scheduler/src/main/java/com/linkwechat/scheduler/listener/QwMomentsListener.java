package com.linkwechat.scheduler.listener;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.domain.moments.query.WeMomentsJobIdToMomentsIdRequest;
import com.linkwechat.domain.moments.query.WeMomentsSyncGroupSendMqRequest;
import com.linkwechat.domain.moments.query.WeMomentsSyncGroupSendRequest;
import com.linkwechat.service.IWeMomentsTaskService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 朋友圈监听
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/09 18:19
 */
@Slf4j
@Component
public class QwMomentsListener {

    @Resource
    private IWeMomentsTaskService weMomentsTaskService;
    @Resource
    private RedisService redisService;

    /**
     * 朋友圈发送任务定时执行
     *
     * @author WangYX
     * @date 2023/06/09 18:22
     * @version 2.0.0
     */
    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.route.delay.we-moments:Qu_Moments_Delay_Execute}")
    public void momentsExecute(String msg, Channel channel, Message message) {
        try {
            log.info("朋友圈发送任务执行处理：msg:{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            momentsExecute(Long.valueOf(msg));

        } catch (Exception e) {
            log.error("朋友圈发送任务定时执行失败 msg:{},error:{}", msg, e);
        }
    }

//    /**
//     * 朋友圈任务定时取消
//     *
//     * @author WangYX
//     * @date 2023/06/09 18:22
//     * @version 2.0.0
//     */
//    @RabbitHandler
//    @RabbitListener(queues = "${wecom.mq.route.delay.we-moments:Qu_Moments_Delay_Cancel}")
//    public void momentsCancel(String msg, Channel channel, Message message) {
//        try {
//            log.info("朋友圈任务定时取消处理：msg:{}", msg);
//            momentsCancel(Long.valueOf(msg));
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        } catch (Exception e) {
//            log.error("朋友圈任务定时取消处理失败 msg:{},error:{}", msg, e);
//        }
//    }


    /**
     * 同步企微朋友圈
     *
     * @author WangYX
     * @date 2023/06/12 10:58
     * @version 2.0.0
     */
    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.sync.we-moments:Qu_Moments}")
    public void momentsDataSync(String msg, Channel channel, Message message) {
        try {
            log.info("企微朋友圈同步消息监听：msg:{}", msg);
            String key = "momentsSyncKey";
            String value = "lock";
            Boolean b = redisService.tryLock(key, value, 60 * 60L);
            if (b) {
                try {
                    weMomentsTaskService.syncWeMomentsHandler(msg);
                } finally {
                    redisService.unLock(key, value);
                }
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("企微朋友圈同步-消息处理失败 msg:{},error:{}", msg, e);
        }
    }

    /**
     * 获取成员群发执行结果
     *
     * @author WangYX
     * @date 2023/07/04 15:03
     * @version 1.0.0
     */
    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.route.delay.we-moments:Qu_Moments_Get_Group_Send_Result}")
    public void getGroupSendExecuteResult(String msg, Channel channel, Message message) {
        try {
            log.info("获取成员群发执行结果处理：msg:{}", msg);
            WeMomentsSyncGroupSendMqRequest request = JSONObject.parseObject(msg, WeMomentsSyncGroupSendMqRequest.class);
            weMomentsTaskService.getGroupSendExecuteResult(request);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("获取成员群发执行结果失败 msg:{},error:{}", msg, e);
        }
    }


//    /**
//     * 通过jobId换取momentsId
//     *
//     * @author WangYX
//     * @date 2023/06/09 18:22
//     * @version 2.0.0
//     */
//    @RabbitHandler
//    @RabbitListener(queues = "${wecom.mq.route.delay.we-moments:Qu_Moments_JobId_To_MomentsId}")
//    public void getMomentsIdByJobId(String msg, Channel channel, Message message) {
//        try {
//            log.info("通过jobId换取momentsId处理：msg:{}", msg);
//            WeMomentsJobIdToMomentsIdRequest request = JSONObject.parseObject(msg, WeMomentsJobIdToMomentsIdRequest.class);
//            weMomentsTaskService.jobIdToMomentsId(request);
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        } catch (Exception e) {
//            log.error("通过jobId换取momentsId处理失败 msg:{},error:{}", msg, e);
//        }
//    }

    /**
     * 企微朋友圈互动同步消息监听
     *
     * @author WangYX
     * @date 2023/06/12 10:59
     * @version 2.0.0
     */
    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.sync.we-hd-moments:Qu_Hd_Moments}")
    public void weHdMomentsSubscribe(String msg, Channel channel, Message message) {
        try {
            log.info("企微朋友圈互动同步消息监听：msg:{}", msg);
            weMomentsTaskService.syncMomentsInteractHandler(msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("企微朋友圈互动同步-消息处理失败 msg:{},error:{}", msg, e);
        }
    }


    /**
     * 执行发送朋友圈
     *
     * @author WangYX
     * @date 2023/06/09 18:25
     * @version 2.0.0
     */
    private void momentsExecute(Long momentsTaskId) throws IOException {
        weMomentsTaskService.sendWeMoments(momentsTaskId);
    }

    /**
     * 停止发送朋友圈
     *
     * @author WangYX
     * @date 2023/06/09 18:25
     * @version 2.0.0
     */
    private void momentsCancel(Long momentsTaskId) {
        //取消发送朋友圈
        weMomentsTaskService.cancelSendMoments(momentsTaskId);
    }


}
