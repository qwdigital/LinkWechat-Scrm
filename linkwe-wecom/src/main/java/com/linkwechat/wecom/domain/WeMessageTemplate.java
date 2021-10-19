package com.linkwechat.wecom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author danmo
 * @description 应用消息通知入参
 * @date 2021/10/3 11:59
 **/
@ApiModel
@Data
public class WeMessageTemplate {
    @ApiModelProperty("消息类型 文本:text, 图片:image, 语音:voice, 视频:video, 文件:file, 文本卡片:textcard, 图文:news, 图文消息:link, 小程序：miniprogram")
    @NotNull(message = "消息类型不能为空")
    private String msgType;

    @ApiModelProperty("文本内容（文本消息必传）")
    private String content;

    @ApiModelProperty("素材id（语音、视频、文件 必传）")
    private String mediaId;

    @ApiModelProperty("消息的标题（视频、文本卡片、图文 必传）")
    private String title;

    @ApiModelProperty("消息的描述（视频、文本卡片、图文 必传）")
    private String description;

    @ApiModelProperty("点击后跳转的链接。最长2048字节，请确保包含了协议头(http/https) （文本卡片、图文 必传）")
    private String linkUrl;

    @ApiModelProperty("图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。（文本卡片、图文 必传）")
    private String picUrl;

    @ApiModelProperty("小程序appid（可以在微信公众平台上查询），必须是关联到企业的小程序应用")
    private String appId;
}
