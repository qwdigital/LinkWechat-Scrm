package com.linkwechat.wecom.domain.dto.message;

import lombok.Data;

@Data
public class TextMessageDto {

    /**
     * 消息文本内容，最多4000个字节
     */
    private String content;
}
