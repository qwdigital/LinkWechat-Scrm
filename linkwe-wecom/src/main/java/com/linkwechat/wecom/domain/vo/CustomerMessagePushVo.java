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
     * 发送情况
     * ps:显示样式
     *
     * 1）定时任务 发送时间:2020-12-25 10:00:00
     * 2) 预计发送3190人，已成功发送0人
     * 3）预计发送2个群，已成功发送0个群
     *
     */
    private String sendInfo;

    /**
     * msgid 可以用于获取发送结果
     */
    private String msgid;


}
