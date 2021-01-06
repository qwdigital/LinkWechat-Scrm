package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2020/12/28 14:07
 */
@Data
@TableName(value = "we_sensitive_audit_scope")
@ApiModel(value = "敏感词审计范围")
public class WeSensitiveAuditScope implements Serializable {
    private static final long serialVersionUID = 7696067707890730540L;

    @TableId
    @ApiModelProperty(value = "审计范围id")
    private Long id;

    @TableField(value = "sensitive_id")
    @ApiModelProperty(value = "敏感词表主键")
    private Long sensitiveId;

    /**
     * 1:组织机构id,2:成员id
     */
    @TableField(value = "scope_type")
    @ApiModelProperty(value = "审计范围类型, 1 组织机构 2 成员")
    private Integer scopeType;

    /**
     * 审计对象id
     */
    @TableField(value = "audit_scope_id")
    @ApiModelProperty(value = "审计对象id")
    private String auditScopeId;

    /**
     * 审计对象名称
     */
    @TableField(value = "audit_scope_name")
    @ApiModelProperty(value = "审计对象名称")
    private String auditScopeName;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("scopeType", getScopeType())
                .append("auditScopeId", getAuditScopeId())
                .append("auditScopeName", getAuditScopeName())
                .toString();
    }
}
