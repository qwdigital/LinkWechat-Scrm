package com.linkwechat.wecom.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.linkwechat.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 老客户标签建群客户统计
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("we_pres_tag_group_stat")
public class WePresTagGroupTaskStat extends BaseEntity {

    /**
     * 老客户标签建群任务id
     */
    private Long taskId;

    /**
     * 客户id
     */
    private String customerName;

    /**
     * 是否已送达
     */
    private boolean isSent;

    /**
     * 是否已经在群
     */
    private boolean isInGroup;
}
