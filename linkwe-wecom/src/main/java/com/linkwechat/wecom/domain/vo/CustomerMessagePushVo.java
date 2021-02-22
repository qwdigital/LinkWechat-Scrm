package com.linkwechat.wecom.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 群发消息任务
 */
@Data
public class CustomerMessagePushVo {

    /**
     * 消息id
     */
    private Long messageId;

    /**
     * 消息类型 0 文本消息  1 图片消息 2 链接消息   3 小程序消息
     */
    private String messageType;

    /**
     * 群发任务的类型，默认为single，表示发送给客户，group表示发送给客户群
     * 0 发送给客户 1 发送给客户群
     */
    private String chatType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 群发类型 0 发给客户 1 发给客户群
     */
    private String pushType;

    /**
     * 消息范围 0 全部客户  1 指定客户
     */
    private String pushRange;

    /**
     * 创建人
     */
    private String sender;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /**
     * msgid 可以用于获取发送结果
     */
    private String msgid;

    /**
     * 发送时间
     */
    private String settingTime;

    /**
     * 预计发送消息数（客户对应多少人 客户群对应多个群）
     */
    private Integer expectSend;

    /**
     * 实际发送消息数（客户对应多少人 客户群对应多个群）
     */
    private Integer actualSend;

    /**
     * 是否定时任务 0 常规 1 定时发送
     */
    private Integer timedTask;

    /**
     * 消息发送状态 0 未发送  1 已发送
     */
    private String checkStatus;


}
