package com.linkwechat.domain.sop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * sop素材附件
 * @TableName we_sop_attachments
 */
@TableName(value ="we_sop_attachments")
@Data
public class WeSopAttachments extends BaseEntity {
    /**
     * 主键id
     */
    @TableId
    private Long id;


    /**
     * sop基础id
     */
    private Long sopBaseId;

    /**
     * 来源 1:手动添加的 2:设置sop结束条件时附加的素材
     */
    private Integer source;

    /**
     * 推送时间周期
     */
    private Long sopPushTimeId;

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
     * 删除标识 0 有效 1删除
     */
    @TableLogic
    private Integer delFlag;


}