package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 群发消息附件对象 we_group_message_attachments
 * 
 * @author ruoyi
 * @date 2021-10-19
 */
@ApiModel
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("we_group_message_attachments")
public class WeGroupMessageAttachments extends BaseEntity{
    /** 主键id */
    @ApiModelProperty("id")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 群发消息模板id
     */
    @ApiModelProperty("群发消息模板id")
    @Excel(name = "群发消息模板id")
    private Long msgTemplateId;

    /** 企业群发消息的id */
    @ApiModelProperty("企业群发消息的id")
    @Excel(name = "企业群发消息的id")
    private String msgId;

    @ApiModelProperty("消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file ")
    /** 消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file  */
    @Excel(name = "消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file ")
    private String msgType;

    /** 消息内容 */
    @ApiModelProperty("消息内容")
    @Excel(name = "消息内容")
    private String content;

    /** 媒体id */
    @ApiModelProperty("媒体id")
    @Excel(name = "媒体id")
    private String mediaId;

    /** 消息标题 */
    @ApiModelProperty("消息标题")
    @Excel(name = "消息标题")
    private String title;

    /** 消息描述 */
    @ApiModelProperty("消息描述")
    @Excel(name = "消息描述")
    private String description;

    /** 文件路径 */
    @ApiModelProperty("文件路径")
    @Excel(name = "文件路径")
    private String fileUrl;

    /** 消息链接 */
    @ApiModelProperty("消息链接")
    @Excel(name = "消息链接")
    private String linkUrl;

    /** 消息图片地址 */
    @ApiModelProperty("消息图片地址")
    @Excel(name = "消息图片地址")
    private String picUrl;

    /** 小程序appid */
    @ApiModelProperty("小程序appid")
    @Excel(name = "小程序appid")
    private String appId;
}
