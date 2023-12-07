package com.linkwechat.scheduler.listener;


import com.alibaba.fastjson.JSONObject;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.*;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author danmo
 * @description 企微数据同步监听
 * @date 2022/4/3 15:39
 **/
@Slf4j
@Component
public class QwDataSyncListener {

    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeTagGroupService weTagGroupService;

    @Resource
    private QwSysUserClient qwSysUserClient;

    @Autowired
    private IWeProductService weProductService;

    @Resource
    private IWeProductOrderService weProductOrderService;



    @Autowired
    private IWeLiveService iWeLiveService;

    @Autowired
    private IWeLeaveUserService iWeLeaveUserService;

    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.sync.group-chat:Qu_GroupChat}")
    public void groupChatSubscribe(String msg, Channel channel, Message message) throws IOException {
        try {
            log.info("企微客户群同步消息监听：msg:{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            weGroupService.synchWeGroupHandler(msg);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("企微客户群同步-消息处理失败 msg:{},error:{}", msg, e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
        }
    }


    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.sync.wecustomer:Qu_WeCustomer}")
    public void wecustomerSubscribe(String msg, Channel channel, Message message) {
        try {
            log.info("企微客户同步消息监听：msg:{}", msg);
            weCustomerService.synchWeCustomerHandler(msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("企微客户同步-消息处理失败 msg:{},error:{}", msg, e);
        }
    }


    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.sync.grouptag:Qu_GroupTag}")
    public void groupTagSubscribe(String msg, Channel channel, Message message) {
        try {
            log.info("企微标签同步消息监听：msg:{}", msg);
            weTagGroupService.synchWeGroupTagHandler(msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("企微标签同步-消息处理失败 msg:{},error:{}", msg, e);
        }
    }






    /*@RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.sync.user-depart:Qu_UserDepart}")
    public void weUserAndDepartSubscribe(String msg, Channel channel, Message message) {
        try {
            log.info("企微员工部门同步消息监听：msg:{}", msg);
            qwSysUserClient.sync(msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("企微员工部门同步-消息处理失败 msg:{},error:{}", msg, e);
        }
    }*/

    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.sync.sys-user:Qu_SysUser}")
    public void sysUserSubscribe(String msg, Channel channel, Message message) {
        log.info("企微员工信息消息监听：msg:{}", msg);
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            qwSysUserClient.syncUserHandler(jsonObject);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("企微员工同步消息监听-消息处理失败 msg:{},error:{}", msg, e);
        }
    }


    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.sync.product:Qu_Product}")
    public void weProductSubscribe(String msg, Channel channel, Message message) {
        try {
            log.info("企微商品图册管理同步消息监听：msg:{}", msg);
            weProductService.syncProductListHandle(msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("企微商品图册管理同步-消息处理失败 msg:{},error:{}", msg, e);
        }
    }

    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.sync.product.order:Qu_Product_Order}")
    public void weProductOrderSubscribe(String msg, Channel channel, Message message) {
        try {
            log.info("企微商品图册订单管理同步消息监听：msg:{}", msg);
            weProductOrderService.orderSyncExecute(msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("企微商品图册订单管理同步-消息处理失败 msg:{},error:{}", msg, e);
        }
    }


    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.live-qu:Qu_Live}")
    public void weLiveSubscribe(String msg, Channel channel, Message message) {
        try {
            log.info("企微直播同步消息监听：msg:{}", msg);
            iWeLiveService.synchLiveHandler(msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("企微直播同步-消息处理失败 msg:{},error:{}", msg, e);
        }
    }

    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.leaveUser:Qu_LeaveUser}")
    public void weLeaveUserSubscribe(String msg, Channel channel, Message message) {
        try {
            log.info("企微离职员工同步消息监听：msg:{}", msg);
            iWeLeaveUserService.synchLeaveSysUserHandler(msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("企微离职员工同步消息监听-消息处理失败 msg:{},error:{}", msg, e);
        }
    }

}
