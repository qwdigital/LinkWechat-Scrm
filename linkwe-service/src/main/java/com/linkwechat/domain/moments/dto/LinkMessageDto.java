package com.linkwechat.domain.moments.dto;

import lombok.Data;

@Data
public class LinkMessageDto {

    /**
     * 图文消息标题
     */
    private String title;

    /**
     * 图文消息封面的url
     */
    private String picurl;

    /**
     * 图文消息的描述，最多512个字节
     */
    private String desc;

    /**
     * 图文消息的链接
     */
    private String url;

}