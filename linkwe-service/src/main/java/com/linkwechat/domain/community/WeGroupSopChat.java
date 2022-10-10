package com.linkwechat.domain.community;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群SOP和群关联对象
 */
@Data
@TableName("we_group_sop_chat")
public class WeGroupSopChat extends BaseEntity {

    @TableId
    private Long id;
    /**
     * 群sop id
     */
    private Long ruleId;

    /**
     * 实际群聊id
     */
    private String chatId;

    /**
     * 是否已完成
     */
    private boolean isDone;
}
