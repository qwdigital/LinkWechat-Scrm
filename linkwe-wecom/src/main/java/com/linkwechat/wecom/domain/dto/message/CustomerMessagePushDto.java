package com.linkwechat.wecom.domain.dto.message;

import lombok.Data;

import java.util.List;

/**
 * 群发消息
 */
@SuppressWarnings("all")
@Data
public class CustomerMessagePushDto {

    /**
     * 群发类型 0 发给客户 1 发给客户群
     */
    private String pushType;

    /**
     * 消息范围 0 全部客户  1 指定客户
     */
    private String pushRange;

    /**
     * 客户标签id列表
     */
    private String tag;

    /**
     * 部门id
     */
    private String department;

    /**
     * 员工id
     */
    private String staffId;

    /**
     * 发送时间 为空表示立即发送 ，不为空为指定时间发送
     */
    private String sendTime;

    /**
     * 消息类型 0 文本消息  1 图片消息 2 语音消息  3 视频消息    4 文件消息 5 文本卡片消息 6 图文消息\r\n7 图文消息（mpnews） 8 markdown消息 9 小程序通知消息 10 任务卡片消息
     */
    private String messageType;

    /********************消息内容*******************/

    /**
     * 图片消息
     */
    private ImageMessageDto imageMessage;

    /**
     * 链接消息
     */
    private LinkMessageDto linkMessage;

    /**
     * 文本消息
     */
    private TextMessageDto textMessage;

    /**
     * 小程序消息
     */
    private MiniprogramMessageDto miniprogramMessage;

}
