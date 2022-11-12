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
     * 客户同步队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeSyncWeCustomer() {
        return BindingBuilder.bind(quWeCustomer()).to(syncEx()).with(rabbitMQSettingConfig.getWeCustomerRk()).noargs();
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
    public Binding bindingExchangeSyncWemoments() {
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
    public Binding bindingExchangeSyncHdWemoments() {
        return BindingBuilder.bind(weHdMoments()).to(syncEx()).with(rabbitMQSettingConfig.getWeHdMomentsRk()).noargs();
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
     * 同步商品图册队列
     *
     * @return
     */
    @Bean
    public Queue quProduct() {
        return new Queue(rabbitMQSettingConfig.getWeProductQu());
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
     * 活码队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding bindingExchangeQrCodeChange() {
        return BindingBuilder.bind(quQrCodeChange()).to(qrCodeChangeEx()).with(rabbitMQSettingConfig.getWeQrCodeChangeRk()).noargs();
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
     * @return
     */
    @Bean
    public Binding bindingSopExchange(){
        return BindingBuilder.bind(sopQu()).to(sopEx()).with(rabbitMQSettingConfig.getSopRk()).noargs();
    }


    /**
     * sop队列
     * @return
     */
    @Bean
    public Queue sopQu(){
        return new Queue(rabbitMQSettingConfig.getSopQu());
    }

    /**
     * sop交换机
     * @return
     */
    @Bean
    public Exchange sopEx(){
        // 声明路由交换机，durable:在rabbitmq重启后，交换机还在
        return ExchangeBuilder.fanoutExchange(rabbitMQSettingConfig.getSopEx()).durable(true).build();
    }

}

