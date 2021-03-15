package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 社区运营 - 关键词拉群任务关键词实体
 */
@Data
@NoArgsConstructor
@TableName("we_keyword_group_kw")
public class WeKeywordGroupTaskKeyword {

    /**
     * 关键词拉群任务id
     */
    private Long taskId;

    /**
     * 关键词
     */
    private String keyword;

    public WeKeywordGroupTaskKeyword(Long taskId, String keyword) {
        this.taskId =taskId;
        this.keyword = keyword;
    }
}
