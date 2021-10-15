package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 群与标签关系
 */
@Data
@TableName("we_group_tag_rel")
public class WeGroupTagRel extends BaseEntity {

    //主键
    @TableId
    private String id;
    //群id
    private String chatId;
    //标签id
    private String tagId;

}