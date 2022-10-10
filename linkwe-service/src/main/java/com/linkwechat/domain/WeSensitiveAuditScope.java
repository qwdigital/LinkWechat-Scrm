package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 敏感词审计范围(WeSensitiveAuditScope)
 *
 * @author danmo
 * @since 2022-06-10 10:38:47
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_sensitive_audit_scope")
public class WeSensitiveAuditScope extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 敏感词表主键
     */
    @ApiModelProperty(value = "敏感词表主键")
    @TableField("sensitive_id")
    private Long sensitiveId;


    /**
     * 审计范围类型, 1 组织机构 2 成员
     */
    @ApiModelProperty(value = "审计范围类型, 1 组织机构 2 成员")
    @TableField("scope_type")
    private Integer scopeType;


    /**
     * 审计对象id
     */
    @ApiModelProperty(value = "审计对象id")
    @TableField("audit_scope_id")
    private String auditScopeId;


    /**
     * 审计对象名称
     */
    @ApiModelProperty(value = "审计对象名称")
    @TableField("audit_scope_name")
    private String auditScopeName;


    /**
     * 删除标识 0 正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0 正常 1 删除")
    @TableField("del_flag")
    private Integer delFlag;
}
