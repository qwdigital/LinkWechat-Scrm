package com.linkwechat.domain;


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

import lombok.Data;

/**
 * 欢迎语模板素材表(WeMsgTlpAttachments)
 *
 * @author danmo
 * @since 2022-03-28 10:22:28
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_msg_tlp_attachments")
public class WeMsgTlpAttachments extends QwBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId
    private Long id;


    /**
     * 模板id
     */
    @ApiModelProperty(value = "模板id")
    @TableField("template_id")
    private Long templateId;


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

    
    
    
}
