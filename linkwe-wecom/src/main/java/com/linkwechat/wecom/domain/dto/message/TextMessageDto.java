package com.linkwechat.wecom.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TextMessageDto {

    /**
     * 消息文本内容，最多4000个字节
     */
    private String content;
}
