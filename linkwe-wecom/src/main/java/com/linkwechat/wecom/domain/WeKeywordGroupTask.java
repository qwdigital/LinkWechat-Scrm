package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 社区运营 - 关键词拉群任务实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("we_keyword_group")
public class WeKeywordGroupTask extends BaseEntity {

    /**
     * 主键id
     */
    @TableId
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 群活码id
     */
    private Long groupCodeId;


    /**
     * 加群引导语
     */
    private String welcomeMsg;
}
