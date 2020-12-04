package com.linkwechat.wecom.domain.dto.message;

import lombok.Data;

@Data
public class MiniprogramMessageDto {

    /**
     * 小程序消息标题，最多64个字节
     */
    private String title;

    /**
     * 小程序消息封面的mediaid，封面图建议尺寸为520*416
     */
    private String pic_media_id;

    /**
     * 小程序appid，必须是关联到企业的小程序应用
     */
    private String appid;

    /**
     * 小程序page路径
     */
    private String page;

}
