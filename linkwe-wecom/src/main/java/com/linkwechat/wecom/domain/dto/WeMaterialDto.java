package com.linkwechat.wecom.domain.dto;

import lombok.Data;

/**
 * @description: 企业微信上传临时素材传输实体
 * @author: KEWEN
 * @create: 2020-09-21 21:17
 **/
@Data
public class WeMaterialDto extends WeResultDto {

    /**
     * 媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件(file)
     */
    private String type;

    /**
     * 媒体文件上传后获取的唯一标识，3天内有效
     */
    private String media_id;

    /**
     * 媒体文件上传时间戳
     */
    private String created_at;

}
