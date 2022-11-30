package com.linkwechat.domain.live;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 活码附件表
 * @TableName we_live_attachments
 */
@TableName(value ="we_live_attachments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeLiveAttachments extends BaseEntity{
    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 直播主表id
     */
    private Long liveId;

    /**
     * 消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file 
     */
    private String msgType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 媒体id
     */
    private String mediaId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息描述
     */
    private String description;

    /**
     * 文件路径
     */
    private String fileUrl;

    /**
     * 消息链接
     */
    private String linkUrl;

    /**
     * 消息图片地址
     */
    private String picUrl;

    /**
     * 小程序appid
     */
    private String appId;


    /**
     * 企业群发消息的id，可用于获取群发消息发送结果,手动发送方式没有
     */
    private String msgId;


    /**
     * 素材id
     */
    private Long materialId;


    /**
     * 素材真实类型
     */
    private Integer realType;


    /**
     * 删除标识 0 有效 1删除
     */
    private Integer delFlag;


}