package com.linkwechat.common.constant;

/**
 * 消息通知
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/20 15:17
 */
public class MessageConstants {


    /******************线索********************/
    /**
     * 如：管理员分配了1个线索客户 @张三 给你
     */
    public static final String LEADS_ALLOCATION = "管理员分配了1个线索客户 @{} 给你";

    /**
     * 如：1个线索客户 @李四 超过期限未跟进已自动回收
     */
    public static final String LEADS_AUTO_RECOVERY = "1个线索客户 @{} 超过期限未跟进已自动回收";

    /**
     * 线索长时间待跟进-文本卡片消息内容
     * <p>
     * 如：
     * <p>
     * 2023年07月21日
     * <p>
     * 客户：张三
     */
    public static final String LEADS_LONG_TIME_NOT_FOLLOW_UP = "<div class=\"gray\">{}</div> <div class=\"normal\">客户：{}</div>";

    /**
     * 线索约定事项待跟进-文本卡片消息内容
     * OR 成员的线索约定事项待跟进-文本卡片消息内容
     * <p>
     * 如：
     * <p>
     * 2023年07月21日
     * <p>
     * 客户：张三
     */
    public static final String LEADS_COVENANT_WAIT_FOLLOW_UP = "<div class=\"gray\">{}</div> <div class=\"normal\">客户：{}</div>";

    /**
     * 成员线索跟进@了你-文本卡片消息内容
     * <p>
     * 如：
     * <p>
     * 客户：张三
     * </p>
     * <p>
     * 员工：赵四
     * </p>
     * </p>
     */
    public static final String LEADS_USER_FOLLOW_UP_2_YOU = "<div class=\"gray\">{}</div> <div class=\"normal\">客户：{}</div> <div class=\"normal\">成员：{}</div>";

    /**
     * 有协助跟进的成员回复了你
     * <p>
     * 如：
     * <p>
     * 2023年07月25日
     * </p>
     * <p>
     * 成员：张三
     * </p>
     * </p>
     */
    public static final String LEADS_ASSIST_USER_FOLLOW_UP_REPLY_YOU = "<div class=\"gray\">{}</div> <div class=\"normal\">成员：{}</div>";

    /******************客户********************/
    /**
     * 如：客户 @王五 刚刚添加了您
     */
    public static final String CUSTOMER_ADD = "客户 @{} 刚刚添加了您";
    /**
     * 如：客户 @赵六 刚刚删除了您
     */
    public static final String CUSTOMER_DELETE = "客户 @{} 刚刚删除了您";


    /******************素材********************/
    /**
     * 如: 客户 @李七 查看了您发送的素材【市场调查报告】【10分20秒】
     */
    public static final String MATERIAL_LOOK = "客户 @{} 查看了您发送的素材【{}】【{}】";


    /******************客服********************/
    /**
     * 如:【微信客服】用户【林久】发送咨询消息，你已超过【56秒】未回复，请尽快处理
     */
    public static final String KF_TIME_OUT = "【微信客服】用户【{}】发送咨询消息，你已超过【{}】未回复，请尽快处理";


    /******************客群********************/
    /**
     * 如：客户 @许十 刚刚进入群聊【内部沟通群】
     */
    public static final String GROUP_ADD = "客户 @{} 刚刚进入群聊【{}】";
    /**
     * 如：客户 @江十一 刚刚退出群聊【内部沟通群】
     */
    public static final String GROUP_DELETE = "客户 @{} 刚刚退出群聊【{}】";
}
