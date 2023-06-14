package com.linkwechat.domain.moments.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 同步成员群发
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/13 9:38
 */
@Data
public class WeMomentsSyncGroupSendRequest {

    /**
     * 朋友圈任务Id
     */
    private Long weMomentsTaskId;

    /**
     * 发送时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime sendTime;
}
