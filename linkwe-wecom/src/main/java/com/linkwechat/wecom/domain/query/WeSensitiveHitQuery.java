package com.linkwechat.wecom.domain.query;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/1/4 21:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeSensitiveHitQuery extends BaseEntity {
    /**
     * 1:组织机构id,2:成员id
     */
    @ApiModelProperty(value = "审计范围类型, 1 组织机构 2 成员")
    private Integer scopeType;

    /**
     * 审计对象id
     */
    @ApiModelProperty(value = "审计范围id")
    private String auditScopeId;

    /**
     * 关键词
     */
    @ApiModelProperty(value = "关键词")
    private String keyword;
}
