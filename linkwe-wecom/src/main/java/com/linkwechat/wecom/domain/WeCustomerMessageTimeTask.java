package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群发消息定时任务表
 * @author Kewen
 */
@Data
@TableName("we_customer_messageTimeTask")
public class WeCustomerMessageTimeTask extends BaseEntity {

    public WeCustomerMessageTimeTask(Long messageId, String messageInfo, String customersInfo, String groupsInfo, Long settingTime) {
        this.messageId = messageId;
        this.messageInfo = messageInfo;
        this.customersInfo = customersInfo;
        this.groupsInfo = groupsInfo;
        this.settingTime = settingTime;
    }

    public WeCustomerMessageTimeTask() {
    }

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 消息id
     */
    private Long messageId;

    /**
     * 消息原始信息
     */
    private String messageInfo;

    /**
     * 客户信息列表
     */
    private String customersInfo;

    /**
     * 客户群组信息列表
     */
    private String groupsInfo;

    /**
     * 定时时间的毫秒数
     */
    private Long settingTime;

}
