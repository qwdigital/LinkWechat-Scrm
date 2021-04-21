package com.linkwechat.wecom.domain.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 会话对象返回值
 * @date 2021/4/21 17:18
 **/
@ApiModel
@Data
public class ConversationArchiveVo {
    @ApiModelProperty("消息id")
    private String msgId;

    @ApiModelProperty("音频id")
    private String voiceId;

    @ApiModelProperty("消息发送时间戳")
    private Long time;

    @ApiModelProperty("具体为切换企业的成员的userid")
    private String user;

    @ApiModelProperty("消息动作，目前有send(发送消息)/recall(撤回消息)/switch(切换企业日志)三种类型")
    private String action;

    @ApiModelProperty("消息发送方id。同一企业内容为userid，非相同企业为external_userid。消息如果是机器人发出，也为external_userid")
    private String from;

    @ApiModelProperty("消息发送方详情")
    private JSONObject fromInfo;

    @ApiModelProperty("消息接收方列表，可能是多个，同一个企业内容为userid，非相同企业为external_userid")
    private List<String> toList;

    @ApiModelProperty("消息接收方详情")
    private JSONObject toListInfo;

    @ApiModelProperty("群聊消息的群id。如果是单聊则为空")
    private String roomId;

    @ApiModelProperty("消息发送时间戳，utc时间，ms单位。")
    private Long msgTime;

    @ApiModelProperty("消息类型")
    private String msgType;

    @ApiModelProperty("文本消息内容")
    private Content text;

    @ApiModelProperty("图片消息内容")
    private Content image;

    @ApiModelProperty("撤回消息")
    private Content revoke;

    @ApiModelProperty("同意会话聊天内容")
    private Content disagree;

    @ApiModelProperty("语音内容")
    private Content voice;

    @ApiModelProperty("视频内容")
    private Content video;

    @ApiModelProperty("名片")
    private Content card;

    @ApiModelProperty("位置")
    private Content location;

    @ApiModelProperty("表情")
    private Content emotion;

    @ApiModelProperty("文件")
    private Content file;

    @ApiModelProperty("链接")
    private Content link;

    @ApiModelProperty("小程序消息")
    private Content weApp;

    @ApiModelProperty("会话记录消息")
    private Content chatReCord;

    @ApiModelProperty("待办消息")
    private Content todo;

    @ApiModelProperty("投票消息")
    private Content vote;

    @ApiModelProperty("填表消息")
    private Collect collect;

    @ApiModelProperty("红包消息")
    private RedPacket redPacket;

    @ApiModelProperty("会议邀请消息")
    private Meeting meeting;

    @ApiModelProperty("在线文档消息")
    private Content doc;

    @ApiModelProperty("MarkDown格式消息/图文消息")
    private JSONObject info;

    @ApiModelProperty("混合消息")
    private JSONObject mixed;

    @ApiModelProperty("音频存档消息")
    private JSONObject meetingVoiceCall;

    @ApiModelProperty("日程消息")
    private Calendar calendar;

    @ApiModel
    @Data
    private class Content{
        @ApiModelProperty("文本消息内容")
        private String content;

        @ApiModelProperty("图片资源的md5值")
        private String md5Sum;

        @ApiModelProperty("媒体资源的id信息")
        private String sdkFileId;

        @ApiModelProperty("文件大小")
        private Long fileSize;

        @ApiModelProperty("标识撤回的原消息的msgid")
        private String preMsgId;

        @ApiModelProperty("同意/不同意协议者的userid，外部企业默认为external_userid")
        private String userId;

        @ApiModelProperty("同意/不同意协议的时间 ms单位")
        private Long agreeTime;

        @ApiModelProperty("语音消息大小")
        private Long voiceSize;

        @ApiModelProperty("播放长度")
        private Integer playLength;

        @ApiModelProperty("名片所有者所在的公司名称")
        private String corpName;

        @ApiModelProperty("经度")
        private Double longitude;

        @ApiModelProperty("纬度")
        private Double latitude;

        @ApiModelProperty("地址信息")
        private String address;

        @ApiModelProperty("标题")
        private String title;

        @ApiModelProperty("缩放比例")
        private Integer zoom;

        @ApiModelProperty("表情类型，png或者gif.1表示gif 2表示png")
        private Integer type;

        @ApiModelProperty("表情图片宽度")
        private Integer width;

        @ApiModelProperty("表情图片高度")
        private Integer height;

        @ApiModelProperty("资源的文件大小")
        private Integer imageSize;

        @ApiModelProperty("文件名称")
        private String fileName;

        @ApiModelProperty("文件类型后缀")
        private String fileExt;

        @ApiModelProperty("消息描述")
        private String description;

        @ApiModelProperty("链接url地址")
        private String linkUrl;

        @ApiModelProperty("链接图片url")
        private String imageUrl;

        @ApiModelProperty("用户名称")
        private String userName;

        @ApiModelProperty("小程序名称")
        private String displayName;

        @ApiModelProperty("消息记录内的消息内容")
        private List<Item> item;

        @ApiModelProperty("投票主题")
        private String voteTitle;

        @ApiModelProperty("投票选项，可能多个内容")
        private List<String> voteItem;

        @ApiModelProperty("投票类型.101发起投票、102参与投票")
        private Integer voteType;

        @ApiModelProperty("投票id，方便将参与投票消息与发起投票消息进行前后对照")
        private String voteId;

        @ApiModelProperty("在线文档创建者。本企业成员创建为userid；外部企业成员创建为external_userid")
        private String docCreator;
    }

    @Data
    @ApiModel
    private class Item{
        @ApiModelProperty("每条聊天记录的具体消息类型：ChatRecordText/ ChatRecordFile/ ChatRecordImage/ " +
                "ChatRecordVideo/ ChatRecordLink/ ChatRecordLocation/ ChatRecordMixed")
        private String type;

        @ApiModelProperty("消息时间，utc时间，单位秒")
        private Long msgTime;

        @ApiModelProperty("消息内容。Json串，内容为对应类型的json")
        private String content;

        @ApiModelProperty("是否来自群会话")
        private Boolean fromChatRoom;
    }

    @Data
    @ApiModel
    private class Calendar{
        @ApiModelProperty("日程主题")
        private String title;

        @ApiModelProperty("日程备注")
        private String creatorName;

        @ApiModelProperty("日程参与人")
        private List<String> attendeeName;

        @ApiModelProperty("日程开始时间")
        private Long startTime;

        @ApiModelProperty("日程结束时间")
        private Long endTime;

        @ApiModelProperty("日程地点")
        private String place;

        @ApiModelProperty("日程备注")
        private String remarks;
    }

    @Data
    @ApiModel
    private class Collect{
        @ApiModelProperty("填表消息所在的群名称")
        private String roomName;

        @ApiModelProperty("创建者在群中的名字")
        private String creator;

        @ApiModelProperty("创建的时间")
        private Long createTime;

        @ApiModelProperty("表名")
        private String title;

        @ApiModelProperty("表内容")
        private String details;

        @ApiModelProperty("表项id")
        private Long id;

        @ApiModelProperty("表项名称")
        private String ques;

        @ApiModelProperty("表项类型，有Text(文本),Number(数字),Date(日期),Time(时间)")
        private String type;
    }

    @Data
    @ApiModel
    private class RedPacket{
        @ApiModelProperty("红包总金额 单位为分")
        private Integer totalAmount;

        @ApiModelProperty("红包总个数")
        private Integer totalCnt;

        @ApiModelProperty("红包祝福语")
        private String wish;

        @ApiModelProperty("红包消息类型。1 普通红包、2 拼手气群红包、3 激励群红包")
        private Integer type;
    }

    @Data
    @ApiModel
    private class Meeting{
        @ApiModelProperty("会议主题")
        private String topic;

        @ApiModelProperty("红包总个数")
        private Long startTime;

        @ApiModelProperty("会议结束时间")
        private Long endTime;

        @ApiModelProperty("会议地址")
        private String address;

        @ApiModelProperty("会议备注")
        private String remarks;

        @ApiModelProperty("会议消息类型。101发起会议邀请消息、102处理会议邀请消息")
        private String meetingType;

        @ApiModelProperty("会议id。方便将发起、处理消息进行对照")
        private Long meetingId;

        @ApiModelProperty("会议邀请处理状态。1 参加会议、2 拒绝会议、3 待定、4 未被邀请、5 会议已取消、6 会议已过期、7 不在房间内。" +
                "只有meetingtype为102的时候此字段才有内容。")
        private Integer status;
    }
}
