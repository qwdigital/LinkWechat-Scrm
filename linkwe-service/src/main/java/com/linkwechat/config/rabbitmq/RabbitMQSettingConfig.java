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
     * 客户群新增成员通知交换机
     */
    @Value("${wecom.mq.exchange.group-add-user:Ex_GroupAddUser}")
    private String groupAddUserEx;


    /**
     * ai消息交换机
     */
    @Value("${wecom.mq.exchange.ai-msg:Ex_AiMsg}")
    private String aiMsgEx;


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


    //新客sop路由
    @Value("${wecom.mq.route.sync.wecustomer:Rk_Wecustomer_Sop}")
    private String newWeCustomerSopRk;


    //新群sop路由
    @Value("${wecom.mq.route.sync.wecustomer:Rk_Group_Sop}")
    private String newWeGroupSopRk;


    //转入下一个客户sop路由
    @Value("${wecom.mq.route.sync.wecustomer:Rk_Change_Wecustomer_Sop}")
    private String chnageWeCustomerSopRk;


    //客户详情同步路由
    @Value("${wecom.mq.route.sync.wecustomer:Rk_Wecustomer_Detail}")
    private String weCustomerDetailRk;


    //离职成员分配同步路由
    @Value("${wecom.mq.route.sync.leaveUser:Rk_leaveUser}")
    private String weLeaveAllocateUserRk;

    //客户标签同步路由
    @Value("${wecom.mq.route.sync.group-tag:Rk_GroupTag}")
    private String weGroupTagRk;

    //员工与部门同步路由
    @Value("${wecom.mq.route.sync.user-depart:Rk_UserDepart}")
    private String userDepartRk;

    //员工同步路由
    @Value("${wecom.mq.route.sync.sys-user:Rk_SysUser}")
    private String sysUserRk;

    //朋友圈同步路由
    /**
     * 朋友圈同步路由
     */
    @Value("${wecom.mq.route.sync.we-moments:Rk_Moments}")
    private String weMomentsRk;
    /**
     * 朋友圈互动同步路由
     */
    @Value("${wecom.mq.route.sync.we-hd-moments:Rk_Hd_Moments}")
    private String weHdMomentsRk;
    /**
     * 朋友圈定时执行路由
     */
    @Value("${wecom.mq.route.delay.we-moments:Rk_Moments_Delay_Execute}")
    private String weMomentsDelayExecuteRk;
    /**
     * 朋友圈定时取消路由
     */
    @Value("${wecom.mq.route.delay.we-moments:Rk_Moments_Delay_Cancel}")
    private String weMomentsDelayCancelRk;
    /**
     * 朋友圈JobId换取MomentId路由
     */
    @Value("${wecom.mq.route.delay.we-moments:Rk_Moments_JobId_To_MomentsId}")
    private String weMomentsDelayJobIdToMomentsIdRK;
    /**
     * 获取成员群发执行结果路由
     */
    @Value("${wecom.mq.route.delay.we-moments:Rk_Moments_Get_Group_Send_Result}")
    private String weMomentsDelayGetGroupSendResultRK;

    /**
     * 待办任务路由
     */
    @Value("${wecom.mq.route.delay.we-tasks:Rk_Tasks}")
    private String weTasksDelayRk;

    //客服账号同步路由
    @Value("${wecom.mq.route.sync.kf-account:Rk_KfAccount}")
    private String weKfAccountRk;

    //群发延迟消息路由
    @Value("${wecom.mq.route.delay-group-msg:Rk_DelayGroupMsg}")
    private String weDelayGroupMsgRk;

    //群发消息结束路由-短链推广
    @Value("${wecom.mq.route.delay-group-msg-end:Rk_DelayGroupMsgEnd}")
    private String weDelayGroupMsgEndRk;

    //企微朋友圈延迟路由
    @Value("${wecom.mq.route.delay-moment-msg:Rk_DelayMomentMsg}")
    private String weDelayMomentMsgRk;

    //群发消息路由
    @Value("${wecom.mq.route.group-msg:Rk_GroupMsg}")
    private String weGroupMsgRk;

    //企微朋友圈路由
    @Value("${wecom.mq.route.moment-msg:Rk_MomentMsg}")
    private String weMomentMsgRk;


    //应用消息通知路由
    @Value("${wecom.mq.route.app-msg:Rk_AppMsg}")
    private String weAppMsgRk;

    //延迟应用消息通知路由
    @Value("${wecom.mq.route.delay_app-msg:Rk_Delay_AppMsg}")
    private String weDelayAppMsgRk;

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

    //客服会话客服超时路由
    @Value("${wecom.mq.route.kf-chat-kf-timeout-msg:Rk_KfChatKfTimeOutMsg}")
    private String weKfChatKfTimeOutMsgRk;

    //客服会话结束路由
    @Value("${wecom.mq.route.kf-chat-end-msg:Rk_KfChatEndMsg}")
    private String weKfChatEndMsgRk;

    //会话存档路由
    @Value("${wecom.mq.route.chat-msg-audit:Rk_ChatMsgAudit}")
    private String weChatMsgAuditRk;

    //会话存档审计路由
    @Value("${wecom.mq.route.chat-msg-check:Rk_ChatMsgCheck}")
    private String weChatMsgCheckRk;

    //会话存档规则路由
    @Value("${wecom.mq.route.chat-msg-qi-rule:Rk_ChatMsgQiRule}")
    private String weChatMsgQiRuleRk;

    //活码变更路由
    @Value("${wecom.mq.route.qr-code-change:Rk_QrCodeChange}")
    private String weQrCodeChangeRk;

    //活码删除路由
    @Value("${wecom.mq.route.qr-code-del:Rk_QrCodeDel}")
    private String weQrCodeDelRk;

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

    //商品图册订单同步路由
    @Value("${wecom.mq.route.sync.product.order:Rk_Product_Order}")
    private String weProductOrderRk;

    /**
     * 客户群新增成员群活码业务路由
     */
    @Value("${wecom.mq.route.group-add-user-code:Rk_GroupAddUserCode}")
    private String groupAddUserCodeRk;

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

    //应用通知消息延迟消费队列
    @Value("${wecom.mq.queue.delay_app-msg:Qu_Delay_AppMsg}")
    private String weDelayAppMsgQu;

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

    //客服会话客服超时消费队列
    @Value("${wecom.mq.queue.kf-chat-kf-timeout-msg:Qu_KfChatKfTimeOutMsg}")
    private String weKfChatKfTimeOutMsgQu;

    //客服会话结束消费队列
    @Value("${wecom.mq.queue.kf-chat-end-msg:Qu_KfChatEndMsg}")
    private String weKfChatEndMsgQu;

    //会话存档消费队列
    @Value("${wecom.mq.queue.chat-msg-audit:Qu_ChatMsgAudit}")
    private String weChatMsgAuditQu;

    //会话存档审计队列
    @Value("${wecom.mq.queue.chat-msg-check:Qu_ChatMsgCheck}")
    private String weChatMsgCheckQu;

    //会话存档规则消费队列
    @Value("${wecom.mq.queue.chat-msg-qi-rule:Qu_ChatMsgQiRule}")
    private String weChatMsgQiRule;

    //企业微信客户队列
    @Value("${wecom.mq.queue.sync.wecustomer:Qu_WeCustomer}")
    private String wecustomerQu;

    //企业微信客户详情队列
    @Value("${wecom.mq.queue.sync.wecustomer:Qu_Detail_WeCustomer}")
    private String wecustomerDetailQu;

    //新客sop队列
    @Value("${wecom.mq.queue.sync.sop:Qu_NewWeCustomerSopQu}")
    private String newWeCustomerSopQu;


    //新群sop队列
    @Value("${wecom.mq.queue.sync.sop:Qu_NewWeGroupSopQu}")
    private String newWeGroupSopQu;

    //转入下一个客户sop队列
    @Value("${wecom.mq.queue.sync.sop:Qu_Chnage_WeCustomerSopQu}")
    private String changeWeCustomerSopQu;


    //企业微信标签队列
    @Value("${wecom.mq.queue.sync.grouptag:Qu_GroupTag}")
    private String grouptagQu;

    /**
     * 朋友圈同步队列
     */
    @Value("${wecom.mq.queue.sync.we-moments:Qu_Moments}")
    private String weMomentsQu;
    /**
     * 朋友圈互动同步队列
     */
    @Value("${wecom.mq.queue.sync.we-hd-moments:Qu_Hd_Moments}")
    private String weHdMomentsQu;
    /**
     * 朋友圈定时发送队列
     */
    @Value("${wecom.mq.queue.delay.we-moments:Qu_Moments_Delay_Execute}")
    private String weMomentsDelayExecuteQu;
    /**
     * 朋友圈定时取消队列
     */
    @Value("${wecom.mq.queue.delay.we-moments:Qu_Moments_Delay_Cancel}")
    private String weMomentsDelayCancelQu;
    /**
     * 朋友圈JobId换取MomentId队列
     */
    @Value("${wecom.mq.route.delay.we-moments:Qu_Moments_JobId_To_MomentsId}")
    private String weMomentsDelayJobIdToMomentsIdQu;
    /**
     * 获取成员群发执行结果队列
     */
    @Value("${wecom.mq.route.delay.we-moments:Qu_Moments_Get_Group_Send_Result}")
    private String weMomentsDelayGetGroupSendResultQu;

    /**
     * 待办任务队列
     */
    @Value("${wecom.mq.queue.delay.we-tasks:Qu_Tasks}")
    private String weTasksDelayQu;

    //员工与部门同步队列
    @Value("${wecom.mq.queue.sync.user-depart:Qu_UserDepart}")
    private String userDepartQu;


    //员工同步队列
    @Value("${wecom.mq.queue.sync.sys-user:Qu_SysUser}")
    private String sysUserQu;

    //活码变更队列
    @Value("${wecom.mq.queue.qr-code-change:Qu_QrCodeChange}")
    private String weQrCodeChangeQu;

    //活码删除队列
    @Value("${wecom.mq.queue.qr-code-del:Qu_QrCodeDel}")
    private String weQrCodeDelQu;

    //计算人群队列
    @Value("${wecom.mq.queue.crowd-calculate:Qu_CrowdCalculate}")
    private String weCrowdCalculateQu;

    //策略延迟消息队列
    @Value("${wecom.mq.queue.delay-journey:Qu_DelayGourney}")
    private String weDelayJourneyQu;

    //商品图册同步队列
    @Value("${wecom.mq.queue.sync.product:Qu_Product}")
    private String weProductQu;

    //商品图册同步队列
    @Value("${wecom.mq.queue.sync.product.order:Qu_Product_Order}")
    private String weProductOrderQu;

    //sop相关数据同步队列
    @Value("${wecom.mq.queue.sop-qu:Qu_Sop}")
    private String sopQu;


    //直播数据同步队列
    @Value("${wecom.mq.queue.live-qu:Qu_Live}")
    private String liveQu;

    //直播同步路由
    @Value("${wecom.mq.route.sync.welive:Rk_WeLive}")
    private String weLiveRk;


    //离职成员分配同步路由
    @Value("${wecom.mq.queue.leaveUser:Qu_LeaveUser}")
    private String leaveAllocateUserQu;

    /**
     * 朋友圈消息队列
     */
    @Value("${wecom.mq.queue.moments-msg:Qu_MomentsMsg}")
    private String MomentsMsgQu;

    /**
     * 朋友圈消息延迟队列
     */
    @Value("${wecom.mq.queue.delay-moments-msg:Qu_DelayMomentsMsg}")
    private String momentsDelayQu;

    /**
     * 群发消息结束队列-短链推广
     */
    @Value("${wecom.mq.queue.delay-group-msg-end:Qu_DelayGroupMsgEnd}")
    private String groupMsgEndDelayQu;
    /**
     * 客户群新增成员群活码业务队列
     */
    @Value("${wecom.mq.queue.group-add-user-code:Qu_GroupAddUserCode}")
    private String groupAddUserCodeQu;


    /**
     * ai消息队列
     */
    @Value("${wecom.mq.queue.ai-msg:Qu_AiMsg}")
    private String aiMsgQu;




}
