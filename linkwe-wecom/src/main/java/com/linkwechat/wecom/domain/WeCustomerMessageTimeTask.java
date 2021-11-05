package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.wecom.domain.dto.message.CustomerMessagePushDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 群发消息定时任务表
 * @author Kewen
 */
@Data
@TableName("we_customer_messageTimeTask")
public class WeCustomerMessageTimeTask extends BaseEntity {

    public WeCustomerMessageTimeTask(Long messageId, CustomerMessagePushDto messageInfo, List<WeCustomerList> customersInfo, List<WeGroup> groupsInfo, Long settingTime) {
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
    private CustomerMessagePushDto messageInfo;

    /**
     * 客户信息列表
     */
    private List<WeCustomerList> customersInfo;

    /**
     * 客户群组信息列表
     */
    private List<WeGroup> groupsInfo;

    /**
     * 定时时间的毫秒数
     */
    private Long settingTime;

    /**
     * 0 未解决 1 已解决
     */
    private Integer solved;

}
