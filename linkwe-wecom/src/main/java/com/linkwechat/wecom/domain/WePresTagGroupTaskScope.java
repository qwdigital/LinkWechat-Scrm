package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 老客户标签建群任务使用人对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("we_pres_tag_group_scope")
public class WePresTagGroupTaskScope {

    /**
     * 老客户标签建群任务id
     */
    private Long taskId;

    /**
     * 任务目标员工id
     */
    private String weUserId;

    /**
     * 是否已处理
     */
    private boolean isDone;

}
