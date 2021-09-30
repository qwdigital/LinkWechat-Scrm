package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群发消息  微信消息发送结果表 we_customer_messgaeResult
 *
 * @author kewen
 * @date 2020-12-05
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_customer_messgaeResult")
public class WeCustomerMessgaeResult extends BaseEntity {

    /**
     * 主键id
     */
    private Long messgaeResultId;

    /**
     * 微信消息表id
     */
    private Long messageId;

    /**
     * 外部联系人userid
     */
    private String externalUserid;

    /**
     * 外部客户群id
     */
    private String chatId;

    /**
     * 外部客户群名称
     */
    private String chatName;

    /**
     * 企业服务人员的userid
     */
    private String userid;

    /**
     * 发送状态 0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
     */
    private String status;

    /**
     * 发送时间，发送状态为1时返回(为时间戳的形式)
     */
    private String sendTime;

    /**
     * 外部联系人名称
     */
    private String externalName;

    /**
     * 企业服务人员的名称
     */
    private String userName;

    /**
     * 0 定时发送 1 发给客户 2 发给客户群
     */
    private String sendType;

    /**
     * 定时发送时间
     */
    private String settingTime;

    /**
     * 0 未删除 1 已删除
     */
    private int delFlag;


}
