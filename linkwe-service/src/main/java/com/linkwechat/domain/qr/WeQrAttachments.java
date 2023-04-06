package com.linkwechat.domain.qr;

import com.baomidou.mybatisplus.annotation.*;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 活码附件表(WeQrAttachments)表实体类
 *
 * @author makejava
 * @since 2021-11-07 01:29:13
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_qr_attachments")
public class WeQrAttachments extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId
    private Long id;


    /**
     * 活码id
     */
    @ApiModelProperty(value = "活码id")
    @TableField("qr_id")
    private Long qrId;

    /**
     * 业务类型 1-员工活码 2-门店导购 3-拉新活码
     */
    @ApiModelProperty(value = "业务类型 1-员工活码 2-门店导购 3-拉新活码 ...")
    @TableField("business_type")
    private Integer businessType;


    /**
     * 消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file
     */
    @ApiModelProperty(value = "消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file ")
    @TableField("msg_type")
    private String msgType;


    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    @TableField("content")
    private String content;


    /**
     * 媒体id
     */
    @ApiModelProperty(value = "媒体id")
    @TableField("media_id")
    private String mediaId;


    /**
     * 消息标题
     */
    @ApiModelProperty(value = "消息标题")
    @TableField("title")
    private String title;


    /**
     * 消息描述
     */
    @ApiModelProperty(value = "消息描述")
    @TableField("description")
    private String description;


    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径")
    @TableField("file_url")
    private String fileUrl;


    /**
     * 消息链接
     */
    @ApiModelProperty(value = "消息链接")
    @TableField("link_url")
    private String linkUrl;


    /**
     * 消息图片地址
     */
    @ApiModelProperty(value = "消息图片地址")
    @TableField("pic_url")
    private String picUrl;


    /**
     * 小程序appid
     */
    @ApiModelProperty(value = "小程序appid")
    @TableField("app_id")
    private String appId;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除",hidden = true)
    @TableField("del_flag")
    @TableLogic
    private Integer delFlag;

    /**
     * 真实媒体类型（企微只支持文本，视频，图片和链接，所以一些别的类型的素材需要转成Link。此处保存的是转成链接之前真实的类型）
     */
    @ApiModelProperty(value = "真实媒体类型")
    @TableField("real_type")
    private Integer realType;

    /**
     * 素材中心中的素材ID
     */
    @ApiModelProperty(value = "真实媒体类型")
    @TableField("material_id")
    private Long materialId;
}
