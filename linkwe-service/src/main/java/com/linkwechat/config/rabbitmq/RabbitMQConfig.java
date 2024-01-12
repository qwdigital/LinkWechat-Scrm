package com.linkwechat.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author danmo
 * @description rabbitmq
 * @date 2022/3/10 11:44
 **/
@Configuration
public class RabbitMQConfig {

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    /**
     * 声明回调交换机
     */
    @Bean
    public Exchange exCallBack() {
        // 声明路由交换机，durable:在rabbitmq重启后，交换机还在
        return ExchangeBuilder.directExchange(rabbitMQSettingConfig.getWeCallBackEx()).durable(true).build();
    }

    /**
     * 声明同步交换机
     */
    @Bean
    public Exchange syncEx() {
        return ExchangeBuilder.directExchange(rabbitMQSettingConfig.getWeSyncEx()).durable(true).build();
    }

    /**
     * 声明延迟交换机
     */
    @Bean
    public Exchange delayEx() {
        // 声明路由交换机，durable:在rabbitmq重启后，交换机还在

        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        //自定义交换机
        return new CustomExchange(rabbitMQSettingConfig.getWeDelayEx(), "x-delayed-message", true, false, args);
    }

    /**
     * 声明应用消息交换机
     */
    @Bean
    public Exchange appMsgEx() {
        // 声明路由交换机，durable:在rabbitmq重启后，交换机还在
        return ExchangeBuilder.directExchange(rabbitMQSettingConfig.getWeAppMsgEx()).durable(true).build();
    }

    /**
     * 欢迎语交换机
     */
    @Bean
    public Exchange welcomeMsgEx() {
        // 声明路由交换机，durable:在rabbitmq重启后，交换机还在
        return ExchangeBuilder.directExchange(rabbitMQSettingConfig.getWeWelcomeMsgEx()).durable(true).build();
    }

    /**
     * 客服会话交换机
     */
    @Bean
    public Exchange kfChatMsgEx() {
        return ExchangeBuilder.directExchange(rabbitMQSettingConfig.getWeKfChatMsgEx()).durable(true).build();
    }

    /**
     * 声明客服延迟交换机
     */
    @Bean
    public Exchange kfChatMsgDelayEx() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        //自定义交换机
        return new CustomExchange(rabbitMQSettingConfig.getWeKfChatMsgDelayEx(), "x-delayed-message", true, false, args);
    }

    /**
     * 会话存档交换机
     */
    @Bean
    public Exchange chatMsgAuditEx() {
        return ExchangeBuilder.fanoutExchange(rabbitMQSettingConfig.getWeChatMsgAuditEx()).durable(true).build();
    }

    /**
     * 活码变更交换机
     *
     * @return
     */
    @Bean
    public Exchange qrCodeChangeEx() {
        return ExchangeBuilder.directExchange(rabbitMQSettingConfig.getWeQrCodeChangeEx()).durable(true).build();
    }


    /**
     * 声明回调队列
     *
     * @return
     */
    @Bean
    public Queue quCallBack() {
        return new Queue(rabbitMQSettingConfig.getWeCallBackQu());
    }

    /**
     * 回调队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeCallback() {
        return BindingBuilder.bind(quCallBack()).to(exCallBack()).with(rabbitMQSettingConfig.getWeCallBackRk()).noargs();
    }

    /**
     * 应用安装广播交换机
     */
    @Bean
    public Exchange weAuthInstallFanoutEx() {
        // 声明路由交换机，durable:在rabbitmq重启后，交换机还在
        return ExchangeBuilder.fanoutExchange(rabbitMQSettingConfig.getWeAuthInstallFanoutEx()).durable(true).build();
    }


    /**
     * 客户群同步队列
     *
     * @return
     */
    @Bean
    public Queue quSyncGroupChat() {
        return new Queue(rabbitMQSettingConfig.getWeGroupChatQu());
    }





    /**
     * 客户群同步队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeSyncGroupChat() {
        return BindingBuilder.bind(quSyncGroupChat()).to(syncEx()).with(rabbitMQSettingConfig.getWeGroupChatRk()).noargs();
    }




    /**
     * 客户同步队列
     *
     * @return
     */
    @Bean
    public Queue quWeCustomer() {
        return new Queue(rabbitMQSettingConfig.getWecustomerQu());
    }



    /**
     * 客户详情同步队列
     *
     * @return
     */
    @Bean
    public Queue quDetailWeCustomer() {
        return new Queue(rabbitMQSettingConfig.getWecustomerDetailQu());
    }


    /**
     * 客户同步队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeSyncWeCustomer() {
        return BindingBuilder.bind(quWeCustomer()).to(syncEx()).with(rabbitMQSettingConfig.getWeCustomerRk()).noargs();
    }


    /**
     * 客户详情同步队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeSyncWeCustomerDetail() {
        return BindingBuilder.bind(quDetailWeCustomer()).to(syncEx()).with(rabbitMQSettingConfig.getWeCustomerDetailRk()).noargs();
    }


    /**
     * 员工部门队列
     *
     * @return
     */
    @Bean
    public Queue quUserDepart() {
        return new Queue(rabbitMQSettingConfig.getUserDepartQu());
    }

    /**
     * 员工部门同步队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeSyncUserDepart() {
        return BindingBuilder.bind(quUserDepart()).to(syncEx()).with(rabbitMQSettingConfig.getUserDepartRk()).noargs();
    }

    /**
     * 员工队列
     *
     * @return
     */
    @Bean
    public Queue quSysUser() {
        return new Queue(rabbitMQSettingConfig.getSysUserQu());
    }

    /**
     * 员工部门同步队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeSyncSysUser() {
        return BindingBuilder.bind(quSysUser()).to(syncEx()).with(rabbitMQSettingConfig.getSysUserRk()).noargs();
    }


    /**
     * 客户标签同步队列
     *
     * @return
     */
    @Bean
    public Queue weGroupTag() {
        return new Queue(rabbitMQSettingConfig.getGrouptagQu());
    }

    /**
     * 客户标签同步队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeSyncWeGroupTag() {
        return BindingBuilder.bind(weGroupTag()).to(syncEx()).with(rabbitMQSettingConfig.getWeGroupTagRk()).noargs();
    }


    /**
     * 朋友圈同步队列
     *
     * @return
     */
    @Bean
    public Queue weMoments() {
        return new Queue(rabbitMQSettingConfig.getWeMomentsQu());
    }

    /**
     * 朋友圈同步队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeSyncWeMoments() {
        return BindingBuilder.bind(weMoments()).to(syncEx()).with(rabbitMQSettingConfig.getWeMomentsRk()).noargs();
    }

    /**
     * 朋友圈互动同步队列
     *
     * @return
     */
    @Bean
    public Queue weHdMoments() {
        return new Queue(rabbitMQSettingConfig.getWeHdMomentsQu());
    }

    /**
     * 朋友圈互动同步队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeSyncHdWeMoments() {
        return BindingBuilder.bind(weHdMoments()).to(syncEx()).with(rabbitMQSettingConfig.getWeHdMomentsRk()).noargs();
    }

    /**
     * 朋友圈定时执行队列
     *
     * @return 定时执行队列
     */
    @Bean
    public Queue weMomentsDelayExecute() {
        return new Queue(rabbitMQSettingConfig.getWeMomentsDelayExecuteQu());
    }

    /**
     * 朋友圈定时执行队列绑定延迟交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeDelayMomentDelayExecute() {
        return BindingBuilder.bind(weMomentsDelayExecute()).to(delayEx()).with(rabbitMQSettingConfig.getWeMomentsDelayExecuteRk()).noargs();
    }

    /**
     * 朋友圈定时取消队列
     *
     * @return 定时取消队列
     */
    @Bean
    public Queue weMomentsDelayCancel() {
        return new Queue(rabbitMQSettingConfig.getWeMomentsDelayCancelQu());
    }

    /**
     * 朋友圈定时执行队列绑定延迟交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeDelayMomentDelayCancel() {
        return BindingBuilder.bind(weMomentsDelayCancel()).to(delayEx()).with(rabbitMQSettingConfig.getWeMomentsDelayCancelRk()).noargs();
    }

    /**
     * 朋友圈jobId换取momentsId队列
     *
     * @author WangYX
     * @date 2023/06/13 10:30
     * @version 1.0.0
     */
    @Bean
    public Queue weMomentsJobIdToMomentsId() {
        return new Queue(rabbitMQSettingConfig.getWeMomentsDelayJobIdToMomentsIdQu());
    }

    /**
     * 朋友圈jobId换取momentsId绑定交换机
     *
     * @author WangYX
     * @date 2023/06/13 10:30
     * @version 1.0.0
     */
    @Bean
    public Binding bindingExchangeDelayJobIdToMomentsId() {
        return BindingBuilder.bind(weMomentsJobIdToMomentsId()).to(delayEx()).with(rabbitMQSettingConfig.getWeMomentsDelayJobIdToMomentsIdRK()).noargs();
    }

    /**
     * 获取成员群发执行结果队列
     *
     * @author WangYX
     * @date 2023/07/04 15:14
     * @version 1.0.0
     */
    @Bean
    public Queue weMomentsGetGroupSendResult() {
        return new Queue(rabbitMQSettingConfig.getWeMomentsDelayGetGroupSendResultQu());
    }

    /**
     * 获取成员群发执行结果队列绑定交换机
     *
     * @author WangYX
     * @date 2023/07/04 15:14
     * @version 1.0.0
     */
    @Bean
    public Binding bindingExchangeDelayGetGroupSendResult() {
        return BindingBuilder.bind(weMomentsGetGroupSendResult()).to(delayEx()).with(rabbitMQSettingConfig.getWeMomentsDelayGetGroupSendResultRK()).noargs();
    }


    /**
     * 客服账号同步队列
     *
     * @return
     */
    @Bean
    public Queue quSyncKfAccount() {
        return new Queue(rabbitMQSettingConfig.getWeKfAccountQu());
    }

    /**
     * 客服同步队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeSyncKfAccount() {
        return BindingBuilder.bind(quSyncKfAccount()).to(syncEx()).with(rabbitMQSettingConfig.getWeKfAccountRk()).noargs();
    }

    /**
     * 群发消息延迟队列
     *
     * @return
     */
    @Bean
    public Queue quDelayGroupMsg() {
        return new Queue(rabbitMQSettingConfig.getWeDelayGroupMsgQu());
    }

    /**
     * 群发消息正常队列
     *
     * @return
     */
    @Bean
    public Queue quGroupMsg() {
        return new Queue(rabbitMQSettingConfig.getWeGroupMsgQu());
    }

    /**
     * 应用通知消息队列
     *
     * @return
     */
    @Bean
    public Queue quAppMsg() {
        return new Queue(rabbitMQSettingConfig.getWeAppMsgQu());
    }

    /**
     * 客户欢迎语队列
     *
     * @return
     */
    @Bean
    public Queue quCustomerWelcomeMsg() {
        return new Queue(rabbitMQSettingConfig.getWeCustomerWelcomeMsgQu());
    }

    /**
     * 客服欢迎语队列
     *
     * @return
     */
    @Bean
    public Queue quKfWelcomeMsg() {
        return new Queue(rabbitMQSettingConfig.getWeKfWelcomeMsgQu());
    }


    /**
     * 客服会话队列
     *
     * @return
     */
    @Bean
    public Queue quKfChatMsg() {
        return new Queue(rabbitMQSettingConfig.getWeKfChatMsgQu());
    }

    /**
     * 客服会话超时队列
     *
     * @return
     */
    @Bean
    public Queue quKfChatTimeOutMsg() {
        return new Queue(rabbitMQSettingConfig.getWeKfChatTimeOutMsgQu());
    }

    /**
     * 客服会话客服超时队列
     *
     * @return
     */
    @Bean
    public Queue quKfChatKfTimeOutMsg() {
        return new Queue(rabbitMQSettingConfig.getWeKfChatKfTimeOutMsgQu());
    }

    /**
     * 客服会话结束队列
     *
     * @return
     */
    @Bean
    public Queue quKfChatEndMsg() {
        return new Queue(rabbitMQSettingConfig.getWeKfChatEndMsgQu());
    }

    /**
     * 会话存档队列
     *
     * @return
     */
    @Bean
    public Queue quChatMsgAudit() {
        return new Queue(rabbitMQSettingConfig.getWeChatMsgAuditQu());
    }

    /**
     * 会话存档审计队列
     *
     * @return
     */
    @Bean
    public Queue quChatMsgCheck() {
        return new Queue(rabbitMQSettingConfig.getWeChatMsgCheckQu());
    }

    @Bean
    public Queue quChatMsgQiRule() {
        return new Queue(rabbitMQSettingConfig.getWeChatMsgQiRule());
    }


    /**
     * 活码变更队列
     *
     * @return
     */
    @Bean
    public Queue quQrCodeChange() {
        return new Queue(rabbitMQSettingConfig.getWeQrCodeChangeQu());
    }

    /**
     * 活码删除队列
     *
     * @return
     */
    @Bean
    public Queue quQrCodeDel() {
        return new Queue(rabbitMQSettingConfig.getWeQrCodeDelQu());
    }

    /**
     * 同步商品图册队列
     *
     * @return
     */
    @Bean
    public Queue quProduct() {
        return new Queue(rabbitMQSettingConfig.getWeProductQu());
    }

    /**
     * 同步商品图册订单队列
     *
     * @return
     */
    @Bean
    public Queue quProductOrder() {
        return new Queue(rabbitMQSettingConfig.getWeProductOrderQu());
    }

    /**
     * 群发消息延迟队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeDelayGroupMsg() {
        return BindingBuilder.bind(quDelayGroupMsg()).to(delayEx()).with(rabbitMQSettingConfig.getWeDelayGroupMsgRk()).noargs();
    }

    /**
     * 群发消息正常队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeGroupMsg() {
        return BindingBuilder.bind(quGroupMsg()).to(delayEx()).with(rabbitMQSettingConfig.getWeGroupMsgRk()).noargs();
    }

    /**
     * 应用通知消息队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeAppMsg() {
        return BindingBuilder.bind(quAppMsg()).to(appMsgEx()).with(rabbitMQSettingConfig.getWeAppMsgRk()).noargs();
    }

    /**
     * 客户欢迎语息队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeCustomerWelcomeMsg() {
        return BindingBuilder.bind(quCustomerWelcomeMsg()).to(welcomeMsgEx()).with(rabbitMQSettingConfig.getWeCustomerWelcomeMsgRk()).noargs();
    }

    /**
     * 客服欢迎语息队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeKfWelcomeMsg() {
        return BindingBuilder.bind(quKfWelcomeMsg()).to(welcomeMsgEx()).with(rabbitMQSettingConfig.getWeKfWelcomeMsgRk()).noargs();
    }

    /**
     * 客服会话队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeKfChatMsg() {
        return BindingBuilder.bind(quKfChatMsg()).to(kfChatMsgEx()).with(rabbitMQSettingConfig.getWeKfChatMsgRk()).noargs();
    }


    /**
     * 客服会话超时队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeKfChatTimeOutMsg() {
        return BindingBuilder.bind(quKfChatTimeOutMsg()).to(kfChatMsgDelayEx()).with(rabbitMQSettingConfig.getWeKfChatTimeOutMsgRk()).noargs();
    }

    /**
     * 客服会话客服超时队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeKfChatKfTimeOutMsg() {
        return BindingBuilder.bind(quKfChatKfTimeOutMsg()).to(kfChatMsgDelayEx()).with(rabbitMQSettingConfig.getWeKfChatKfTimeOutMsgRk()).noargs();
    }

    /**
     * 客服会话结束队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeKfChatEndMsg() {
        return BindingBuilder.bind(quKfChatEndMsg()).to(kfChatMsgDelayEx()).with(rabbitMQSettingConfig.getWeKfChatEndMsgRk()).noargs();
    }

    /**
     * 会话存档队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeChatMsgAudit() {
        return BindingBuilder.bind(quChatMsgAudit()).to(chatMsgAuditEx()).with(rabbitMQSettingConfig.getWeChatMsgAuditRk()).noargs();
    }

    /**
     * 会话存档审计队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeChatMsgCheck() {
        return BindingBuilder.bind(quChatMsgCheck()).to(chatMsgAuditEx()).with(rabbitMQSettingConfig.getWeChatMsgCheckRk()).noargs();
    }

    /**
     * 会话存档质检规则队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeChatMsgQiRule() {
        return BindingBuilder.bind(quChatMsgQiRule()).to(chatMsgAuditEx()).with(rabbitMQSettingConfig.getWeChatMsgQiRuleRk()).noargs();
    }

    /**
     * 活码变更队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeQrCodeChange() {
        return BindingBuilder.bind(quQrCodeChange()).to(qrCodeChangeEx()).with(rabbitMQSettingConfig.getWeQrCodeChangeRk()).noargs();
    }

    /**
     * 活码删除队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeQrCodeDel() {
        return BindingBuilder.bind(quQrCodeDel()).to(qrCodeChangeEx()).with(rabbitMQSettingConfig.getWeQrCodeDelRk()).noargs();
    }


    /**
     * 同步商品图册绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeProduct() {
        return BindingBuilder.bind(quProduct()).to(syncEx()).with(rabbitMQSettingConfig.getWeProductRk()).noargs();
    }

    /**
     * sop队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingSopExchange() {
        return BindingBuilder.bind(sopQu()).to(sopEx()).with(rabbitMQSettingConfig.getSopRk()).noargs();
    }


    /**
     * 新客sop队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingNewWeCustomerSopExchange() {
        return BindingBuilder.bind(newWeCustomerSopQu()).to(sopEx()).with(rabbitMQSettingConfig.getNewWeCustomerSopRk()).noargs();
    }

    /**
     * 群sop队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingGroupSopExchange() {
        return BindingBuilder.bind(newGroupSopQu()).to(sopEx()).with(rabbitMQSettingConfig.getNewWeGroupSopRk()).noargs();
    }


    /**
     * 转入下一个sop队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingChangeWeCustomerSopExchange() {
        return BindingBuilder.bind(changeWeCustomerSopQu()).to(sopEx()).with(rabbitMQSettingConfig.getChnageWeCustomerSopRk()).noargs();
    }

    /**
     * sop队列
     *
     * @return
     */
    @Bean
    public Queue sopQu() {
        return new Queue(rabbitMQSettingConfig.getSopQu());
    }


    /**
     * 新客sop队列
     *
     * @return
     */
    @Bean
    public Queue newWeCustomerSopQu() {
        return new Queue(rabbitMQSettingConfig.getNewWeCustomerSopQu());
    }

    /**
     * 新群sop队列
     *
     * @return
     */
    @Bean
    public Queue newGroupSopQu() {
        return new Queue(rabbitMQSettingConfig.getNewWeGroupSopQu());
    }

    /**
     * 转入下一个sop队列
     *
     * @return
     */
    @Bean
    public Queue changeWeCustomerSopQu() {
        return new Queue(rabbitMQSettingConfig.getChangeWeCustomerSopQu());
    }

    /**
     * sop交换机
     *
     * @return
     */
    @Bean
    public Exchange sopEx() {
        // 声明路由交换机，durable:在rabbitmq重启后，交换机还在
        return ExchangeBuilder.fanoutExchange(rabbitMQSettingConfig.getSopEx()).durable(true).build();
    }


    /**
     * 直播交换机
     *
     * @return
     */
    @Bean
    public Exchange liveEx() {
        return ExchangeBuilder.fanoutExchange(rabbitMQSettingConfig.getWeLiveRk()).durable(true).build();
    }

    /**
     * 直播队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeSyncWeLive() {
        return BindingBuilder.bind(quWeLive()).to(syncEx()).with(rabbitMQSettingConfig.getWeLiveRk()).noargs();
    }

    /**
     * 直播同步队列
     *
     * @return
     */
    @Bean
    public Queue quWeLive() {
        return new Queue(rabbitMQSettingConfig.getLiveQu());
    }


    /**
     * 同步商品图册订单绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeProductOrder() {
        return BindingBuilder.bind(quProductOrder()).to(syncEx()).with(rabbitMQSettingConfig.getWeProductOrderQu()).noargs();
    }


    /**
     * 同步离职成员队列
     *
     * @return
     */
    @Bean
    public Queue quLeaveUser() {
        return new Queue(rabbitMQSettingConfig.getLeaveAllocateUserQu());
    }


    /**
     * 离职成员同步绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingQuLeaveUser() {
        return BindingBuilder.bind(quLeaveUser()).to(syncEx()).with(rabbitMQSettingConfig.getWeLeaveAllocateUserRk()).noargs();
    }

    /**
     * 朋友圈消息队列
     *
     * @return
     */
    @Bean
    public Queue quMomentsMsg() {
        return new Queue(rabbitMQSettingConfig.getMomentsMsgQu());
    }

    /**
     * 朋友圈消息队列 绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingQuMoments() {
        return BindingBuilder.bind(quMomentsMsg()).to(delayEx()).with(rabbitMQSettingConfig.getWeMomentMsgRk()).noargs();
    }

    /**
     * 朋友圈消息延迟队列
     *
     * @return
     */
    @Bean
    public Queue quMomentsDelayMsg() {
        return new Queue(rabbitMQSettingConfig.getMomentsDelayQu());
    }

    /**
     * 朋友圈消息延迟队列 绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingQuDelayMoments() {
        return BindingBuilder.bind(quMomentsDelayMsg()).to(delayEx()).with(rabbitMQSettingConfig.getWeDelayMomentMsgRk()).noargs();
    }

    /**
     * 短链推广-应用消息延迟队列
     *
     * @return
     */
    @Bean
    public Queue quDelayAppMsg() {
        return new Queue(rabbitMQSettingConfig.getWeDelayAppMsgQu());
    }

    /**
     * 短链推广-应用消息延迟队列 绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingQuDelayAppMsg() {
        return BindingBuilder.bind(quDelayAppMsg()).to(delayEx()).with(rabbitMQSettingConfig.getWeDelayAppMsgRk()).noargs();
    }

    /**
     * 短链推广-群发消息结束队列
     *
     * @return
     */
    @Bean
    public Queue quDelayGroupMsgEnd() {
        return new Queue(rabbitMQSettingConfig.getGroupMsgEndDelayQu());
    }

    /**
     * 短链推广-群发消息结束队列 绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingQuDelayGroupMsgEnd() {
        return BindingBuilder.bind(quDelayGroupMsgEnd()).to(delayEx()).with(rabbitMQSettingConfig.getWeDelayGroupMsgEndRk()).noargs();
    }


    /**
     * 客户群新增成员广播交换机
     */
    @Bean
    public Exchange weGroupAddUserFanoutEx() {
        return ExchangeBuilder.fanoutExchange(rabbitMQSettingConfig.getGroupAddUserEx()).durable(true).build();
    }

    /**
     * 客户群新增成员群活码业务队列
     *
     * @return
     */
    @Bean
    public Queue quGroupAddUserCode() {
        return new Queue(rabbitMQSettingConfig.getGroupAddUserCodeQu());

    }

    /**
     * 客户群新增成员群活码业务队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingGroupAddUserCode() {

        return BindingBuilder.bind(quGroupAddUserCode()).to(weGroupAddUserFanoutEx()).with(rabbitMQSettingConfig.getGroupAddUserCodeRk()).noargs();

    }

    /**
     * 待办任务
     *
     * @return {@link Queue}
     * @author WangYX
     * @date 2023/07/24 11:19
     */
    @Bean
    public Queue quWeTasksDelay() {
        return new Queue(rabbitMQSettingConfig.getWeTasksDelayQu());
    }
    /**
     * 待办任务绑定延迟交换机
     *
     * @return {@link Binding}
     * @author WangYX
     * @date 2023/07/24 11:22
     */
    @Bean
    public Binding bindingWeTasksDelay() {
        return BindingBuilder.bind(quWeTasksDelay()).to(delayEx()).with(rabbitMQSettingConfig.getWeTasksDelayRk()).noargs();
    }

    /**
     * AI消息队列
     * @return
     */
    @Bean
    public Queue quAiMsg() {
        return new Queue(rabbitMQSettingConfig.getAiMsgQu());
    }

    /**
     * AI广播交换机
     * @return
     */
    @Bean
    public Exchange aiMsgEx() {
        return ExchangeBuilder.topicExchange(rabbitMQSettingConfig.getAiMsgEx()).durable(true).build();
    }

    /**
     * 绑定AI队列
     * @return
     */
    @Bean
    public Binding bindingWeAiMsg() {
        return BindingBuilder.bind(quAiMsg()).to(aiMsgEx()).with("").noargs();
    }
}

