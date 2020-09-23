package com.linkwechat.wecom.domain.dto;

import lombok.Data;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/24 0024 0:16
 */
@Data
public class WeMediaDto extends WeResultDto{
    /**媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件(file)*/
    private String type;
    /**媒体文件上传后获取的唯一标识，3天内有效*/
    private String media_id;
    /**媒体文件上传时间戳*/
    private Long created_at;
    /**上传后得到的图片URL。永久有效*/
    private String url;
}
