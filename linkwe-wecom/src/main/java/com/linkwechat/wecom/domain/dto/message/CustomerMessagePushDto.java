package com.linkwechat.wecom.domain.dto.message;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 群发消息
 */
@SuppressWarnings("all")
@Data
public class CustomerMessagePushDto implements Cloneable{

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
    private String settingTime;

    /**
     * 消息类型 0 文本消息  1 图片消息 2 链接消息   3 小程序消息 4 图文消息
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

    /**
     * 视频
     */
    private VideoDto videoDto;


    /**
     * 是否立即发送
     */
    private boolean isSendNow=false;


    /**
     * 消息内容
     *
     * @param customerMessagePushDto
     * @return
     */
    public String content() {

        if (this.getMessageType()!=null) {
            // 消息类型 0 文本消息  1 图片消息 2 链接消息   3 小程序消息 4 图文
            if ( this.getMessageType().equals("0")) {
                return this.getTextMessage().getContent();
            }
            if (this.getMessageType().equals("1")) {

                return this.getImageMessage().getPic_url();
            }

            if (this.getMessageType().equals("2")) {

                return this.getLinkMessage().getTitle() + ":" + this.getLinkMessage().getPicurl()
                        + ":" + this.getLinkMessage().getUrl() + "" + this.getLinkMessage().getDesc();
            }

            if (this.getMessageType().equals("3")) {

                return this.getMiniprogramMessage().getTitle() + ":" + this.getMiniprogramMessage().getPage();
            }

            if(this.getMessageType().equals("4")){

                return this.getTextMessage().getContent()+","+this.getImageMessage().getPic_url();

            }
        }

        return null;
    }


    @Override
    public CustomerMessagePushDto clone() throws CloneNotSupportedException {
        return (CustomerMessagePushDto) super.clone();
    }




}
