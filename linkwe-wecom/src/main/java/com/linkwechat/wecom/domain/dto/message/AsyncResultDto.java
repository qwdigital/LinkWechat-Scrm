package com.linkwechat.wecom.domain.dto.message;

import lombok.Data;

import java.util.List;

/**
 * 同步群发消息dto
 * @author Kewen
 */
@Data
public class AsyncResultDto {

    private Long messageId;

    private List<String> msgids;

}
