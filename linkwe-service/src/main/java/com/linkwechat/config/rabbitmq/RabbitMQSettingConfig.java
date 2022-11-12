package com.linkwechat.config.rabbitmq;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author danmo
 * @description rabbitmq
 * @date 2022/3/10 11:44
 **/
@Data
@Configuration
public class RabbitMQSettingConfig {
    /**
     * -------------------交换机------------------------------------
     */
    //回调交换机
    @Value("${wecom.mq.exchange.callback:Ex_CallBack}")
    private String weCallBackEx;

    //数据同步交换机
    @Value("${wecom.mq.exchange.sync:Ex_Sync}")
    private String weSyncEx;

    /**
     * 应用安装广播交换机
     */
    @Value("${wecom.mq.exchange.auth-install-fanout:Ex_AuthInstallFanout}")
    private String weAuthInstallFanoutEx;

    /**
     * 延迟队列交换机
     */
    @Value("${wecom.mq.exchange.delay:Ex_Delay}")
    private String weDelayEx;

    /**
     * 应用消息通知交换机
     */
    @Value("${wecom.mq.exchange.app-msg:Ex_AppMsg}")
    private String weAppMsgEx;

    /**
     * 欢迎语通知交换机
     */
    @Value("${wecom.mq.exchange.welcome-msg:Ex_WelcomeMsg}")
    private String weWelcomeMsgEx;

    //客服会话交换机
    @Value("${wecom.mq.exchange.kf-chat-msg:Ex_KfChatMsg}")
    private String weKfChatMsgEx;

    /**
     * 客服延迟队列交换机
     */
    @Value("${wecom.mq.exchange.kf-chat-msg-delay:Ex_KfChatMsgDelay}")
    private String weKfChatMsgDelayEx;

    /**
     * 会话存档交换机
     */
    @Value("${wecom.mq.exchange.chat-msg-audit:Ex_ChatMsgAudit}")
    private String weChatMsgAuditEx;

    /**
     * 员工活码交换器
     */
    @Value("${wecom.mq.exchange.qr-code-change:Ex_QrCodeChange}")
    private String weQrCodeChangeEx;

    /**
     * 策略延迟队列交换机
     */
    @Value("${wecom.mq.exchange.journey.delay:Ex_JourneyDelay}")
    private String weJourneyEx;

    /**
     * sop交换机
     */
    @Value("${wecom.mq.exchange.sop-ex:Ex_Sop}")
    private String sopEx;
    /**
     * -------------------路由------------------------------------
     */
    //自建回调路由
    @Value("${wecom.mq.route.callback:Rk_CallBack}")
    private String weCallBackRk;

    //三方回调路由
    @Value("${wecom.mq.route.third-callback:Rk_Third_CallBack}")
    private String weThirdCallBackRk;

    //客户群同步路由
    @Value("${wecom.mq.route.sync.group-chat:Rk_GroupChat}")
    private String weGroupChatRk;


    //客户同步路由
    @Value("${wecom.mq.route.sync.wecustomer:Rk_Wecustomer}")
    private String weCustomerRk;

    //客户标签同步路由
    @Value("${wecom.mq.route.sync.group-tag:Rk_GroupTag}")
    private String weGroupTagRk;


    //员工与部门同步路由
    @Value("${wecom.mq.route.sync.user-depart:Rk_UserDepart}")
    private String userDepartRk;


    //朋友圈同步路由
    @Value("${wecom.mq.route.sync.we-moments:Rk_Moments}")
    private String weMomentsRk;


    //朋友圈互动同步路由
    @Value("${wecom.mq.route.sync.we-moments:Rk_Hd_Moments}")
    private String weHdMomentsRk;

    //客服账号同步路由
    @Value("${wecom.mq.route.sync.kf-account:Rk_KfAccount}")
    private String weKfAccountRk;

    //群发延迟消息路由
    @Value("${wecom.mq.route.delay-group-msg:Rk_DelayGroupMsg}")
    private String weDelayGroupMsgRk;

    //群发消息路由
    @Value("${wecom.mq.route.group-msg:Rk_GroupMsg}")
    private String weGroupMsgRk;

    //应用消息通知路由
    @Value("${wecom.mq.route.app-msg:Rk_AppMsg}")
    private String weAppMsgRk;

    //客户欢迎语路由
    @Value("${wecom.mq.route.customer-welcome-msg:Rk_CustomerWelcomeMsg}")
    private String weCustomerWelcomeMsgRk;

    //客服欢迎语路由
    @Value("${wecom.mq.route.kf-welcome-msg:Rk_KfWelcomeMsg}")
    private String weKfWelcomeMsgRk;

    //客服会话路由
    @Value("${wecom.mq.route.kf-chat-msg:Rk_KfChatMsg}")
    private String weKfChatMsgRk;

    //客服会话超时路由
    @Value("${wecom.mq.route.kf-chat-timeout-msg:Rk_KfChatTimeOutMsg}")
    private String weKfChatTimeOutMsgRk;

    //客服会话结束路由
    @Value("${wecom.mq.route.kf-chat-end-msg:Rk_KfChatEndMsg}")
    private String weKfChatEndMsgRk;

    //会话存档路由
    @Value("${wecom.mq.route.chat-msg-audit:Rk_ChatMsgAudit}")
    private String weChatMsgAuditRk;

    //会话存档审计路由
    @Value("${wecom.mq.route.chat-msg-check:Rk_ChatMsgCheck}")
    private String weChatMsgCheckRk;

    //活码变更路由
    @Value("${wecom.mq.route.qr-code-change:Rk_QrCodeChange}")
    private String weQrCodeChangeRk;

    //计算人群路由
    @Value("${wecom.mq.route.crowd-calculate:Rk_CrowdCalculate}")
    private String WeCrowdCalculateRk;

    //策略延迟消息路由
    @Value("${wecom.mq.route.delay-journey:Rk_DelayGourney}")
    private String weDelayJourneyRk;


    //商品图册同步路由
    @Value("${wecom.mq.route.sync.product:Rk_Product}")
    private String weProductRk;

    //sop通知路由
    @Value("${wecom.mq.route.sop-rk:rk_sop}")
    private String sopRk;

    /**
     * -------------------队列------------------------------------
     */
    //自建回调队列
    @Value("${wecom.mq.queue.callback:Qu_CallBack}")
    private String weCallBackQu;

    //三方回调队列
    @Value("${wecom.mq.queue.third-callback:Qu_Third_CallBack}")
    private String weThirdCallBackQu;

    //客户群同步消费队列
    @Value("${wecom.mq.queue.sync.group-chat:Qu_GroupChat}")
    private String weGroupChatQu;

    //客服账号同步消费队列
    @Value("${wecom.mq.queue.sync.kf-account:Qu_kfAccount}")
    private String weKfAccountQu;

    //群发延迟消息消费队列
    @Value("${wecom.mq.queue.delay-group-msg:Qu_DelayGroupMsg}")
    private String weDelayGroupMsgQu;

    //群发正常消息消费队列
    @Value("${wecom.mq.queue.group-msg:Qu_GroupMsg}")
    private String weGroupMsgQu;

    //应用通知消息消费队列
    @Value("${wecom.mq.queue.app-msg:Qu_AppMsg}")
    private String weAppMsgQu;

    //客户欢迎语消费队列
    @Value("${wecom.mq.queue.customer-welcome-msg:Qu_CustomerWelcomeMsg}")
    private String weCustomerWelcomeMsgQu;

    //客服欢迎语消费队列
    @Value("${wecom.mq.queue.kf-welcome-msg:Qu_KfWelcomeMsg}")
    private String weKfWelcomeMsgQu;

    //客服会话消费队列
    @Value("${wecom.mq.queue.kf-chat-msg:Qu_KfChatMsg}")
    private String weKfChatMsgQu;

    //客服会话超时消费队列
    @Value("${wecom.mq.queue.kf-chat-timeout-msg:Qu_KfChatTimeOutMsg}")
    private String weKfChatTimeOutMsgQu;


    //客服会话结束消费队列
    @Value("${wecom.mq.queue.kf-chat-end-msg:Qu_KfChatEndMsg}")
    private String weKfChatEndMsgQu;

    //会话存档消费队列
    @Value("${wecom.mq.queue.chat-msg-audit:Qu_ChatMsgAudit}")
    private String weChatMsgAuditQu;

    //会话存档审计队列
    @Value("${wecom.mq.queue.chat-msg-check:Qu_ChatMsgCheck}")
    private String weChatMsgCheckQu;

    //企业微信客户队列
    @Value("${wecom.mq.queue.sync.wecustomer:Qu_WeCustomer}")
    private String wecustomerQu;

    //企业微信标签队列
    @Value("${wecom.mq.queue.sync.grouptag:Qu_GroupTag}")
    private String grouptagQu;


    //朋友圈队列
    @Value("${wecom.mq.queue.sync.we-moments:Qu_Moments}")
    private String weMomentsQu;


    //朋友圈互动队列
    @Value("${wecom.mq.queue.sync.we-moments:Qu_Hd_Moments}")
    private String weHdMomentsQu;


    //员工与部门同步队列
    @Value("${wecom.mq.queue.sync.user-depart:Qu_UserDepart}")
    private String userDepartQu;

    //活码变更队列
    @Value("${wecom.mq.queue.qr-code-change:Qu_QrCodeChange}")
    private String weQrCodeChangeQu;

    //计算人群队列
    @Value("${wecom.mq.queue.crowd-calculate:Qu_CrowdCalculate}")
    private String weCrowdCalculateQu;

    //策略延迟消息队列
    @Value("${wecom.mq.queue.delay-journey:Qu_DelayGourney}")
    private String weDelayJourneyQu;

    //商品图册同步队列
    @Value("${wecom.mq.queue.sync.product:Qu_Product}")
    private String weProductQu;

    //sop相关数据同步队列
    @Value("${wecom.mq.queue.sop-qu:Qu_Sop}")
    private String sopQu;
}
