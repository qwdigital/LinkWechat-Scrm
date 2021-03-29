package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群SOP和群关联对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("we_group_sop_chat")
public class WeGroupSopChat {
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
