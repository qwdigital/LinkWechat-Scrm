package com.linkwechat.domain.message.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 消息通知
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/20 14:42
 */
@Data
public class WeMessageNotificationVo {
    /**
     * 主键Id
     */
    private Long id;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 是否已读 0否 1是
     *
     * @see com.linkwechat.common.enums.message.MessageReadEnum
     */
    private Integer isRead;

    /**
     * 通知时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date notificationTime;

}
