package com.linkwechat.domain.know;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 活码附件表
 * @TableName we_know_customer_attachments
 */
@TableName(value ="we_know_customer_attachments")
@Data
public class WeKnowCustomerAttachments extends BaseEntity {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 识客码id
     */
    private Long knowCustomerId;

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
     * 企业微信端返回的消息id
     */
    private String msgId;

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
     * 素材真实类型
     */
    private Integer realType;

    /**
     * 素材id
     */
    private Long materialId;

    /**
     * 删除标识 0 有效 1删除
     */
    private Integer delFlag;

}