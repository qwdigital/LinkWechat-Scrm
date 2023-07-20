package com.linkwechat.domain.message.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.*;

import java.util.Date;

/**
 * 消息通知
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/20 14:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_message_notification")
public class WeMessageNotification extends BaseEntity {

    /**
     * 主键Id
     */
    @TableId(value = "id")
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
     * 通知时间
     */
    private Date notificationTime;

    /**
     * 员工Id
     */
    private String weUserId;

    /**
     * 是否已读 0否 1是
     *
     * @see com.linkwechat.common.enums.message.MessageReadEnum
     */
    private Integer isRead;

    /**
     * 删除标识
     */
    private Integer delFlag;

}
