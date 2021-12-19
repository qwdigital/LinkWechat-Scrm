package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 老客标签建群任务标签关联对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("we_pres_tag_group_tag")
public class WePresTagGroupTaskTag {

    /**
     * 老客建群任务id
     */
    private Long taskId;

    /**
     * 标签id
     */
    private String tagId;

}
