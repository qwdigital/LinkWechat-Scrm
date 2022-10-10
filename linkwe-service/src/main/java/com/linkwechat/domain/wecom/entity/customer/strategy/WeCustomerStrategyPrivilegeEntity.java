package com.linkwechat.domain.wecom.entity.customer.strategy;

import lombok.Data;

/**
 * @author danmo
 * @Description
 * @date 2021/12/4 0:10
 **/
@Data
public class WeCustomerStrategyPrivilegeEntity {
    /**
     * 查看客户列表，基础权限，不可取消
     */
    private Boolean view_customer_list;
    /**
     * 查看客户统计数据，基础权限，不可取消
     */
    private Boolean view_customer_data;
    /**
     * 查看群聊列表，基础权限，不可取消
     */
    private Boolean view_room_list;
    /**
     * 可使用联系我，基础权限，不可取消
     */
    private Boolean contact_me;
    /**
     * 可加入群聊，基础权限，不可取消
     */
    private Boolean join_room;
    /**
     * 允许分享客户给其他成员，默认为true
     */
    private Boolean share_customer;
    /**
     * 允许分配离职成员客户，默认为true
     */
    private Boolean oper_resign_customer;
    /**
     * 允许分配离职成员客户群，默认为true
     */
    private Boolean oper_resign_group;
    /**
     * 允许给企业客户发送消息，默认为true
     */
    private Boolean send_customer_msg;
    /**
     * 允许配置欢迎语，默认为true
     */
    private Boolean edit_welcome_msg;
    /**
     * 允许查看成员联系客户统计
     */
    private Boolean view_behavior_data;
    /**
     * 允许查看群聊数据统计，默认为true
     */
    private Boolean view_room_data;
    /**
     * 允许发送消息到企业的客户群，默认为true
     */
    private Boolean send_group_msg;
    /**
     * 允许对企业客户群进行去重，默认为true
     */
    private Boolean room_deduplication;
    /**
     * 配置快捷回复，默认为true
     */
    private Boolean rapid_reply;
    /**
     * 转接在职成员的客户，默认为true
     */
    private Boolean onjob_customer_transfer;
    /**
     * 编辑企业成员防骚扰规则，默认为true
     */
    private Boolean edit_anti_spam_rule;
    /**
     * 导出客户列表，默认为true
     */
    private Boolean export_customer_list;
    /**
     * 导出成员客户统计，默认为true
     */
    private Boolean export_customer_data;
    /**
     * 	导出客户群列表，默认为true
     */
    private Boolean export_customer_group_list;
    /**
     * 配置企业客户标签，默认为true
     */
    private Boolean manage_customer_tag;
}
