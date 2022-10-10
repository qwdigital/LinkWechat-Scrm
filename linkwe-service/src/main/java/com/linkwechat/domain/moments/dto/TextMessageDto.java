package com.linkwechat.domain.moments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
