package com.linkwechat.wecom.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

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
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 活码id
     */
    @ApiModelProperty(value = "活码id")
    @TableField("qr_id")
    private Long qrId;


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
    private Integer delFlag;
}
