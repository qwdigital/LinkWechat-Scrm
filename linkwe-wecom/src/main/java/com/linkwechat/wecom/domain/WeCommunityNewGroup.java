package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 社群运营 新客自动拉群
 *
 * @author kewen
 * @date 2021-02-19
 */
@Data
public class WeCommunityNewGroup extends BaseEntity {

    /**
     *主键ID
     */
    private Long newGroupId;

    /**
     *员工活码ID
     */
    private Long empleCodeId;

    /**
     * 员工活码名称
     */
    private String empleCodeName;

    /**
     * 群活码ID
     */
    private Long groupCodeId;

    /**
     * 0 未删除 1 已删除
     */
    private int delFlag;

}
