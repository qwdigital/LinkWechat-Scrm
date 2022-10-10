package com.linkwechat.domain.community;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 新客自动拉群
 */
@Data
@TableName("we_community_new_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCommunityNewGroup extends BaseEntity {

    /**
     *主键ID
     */
    @TableId
    private Long id;

    /**
     * 员工活码名称
     */
    private String emplCodeName;

    /**
     * 群活码id
     */
    private Long groupCodeId;

    /**
     * 员工活码id
     */
    private Long emplCodeId;

    /**
     * 0 未删除 1 已删除
     */
    @TableLogic
    private int delFlag;
}
