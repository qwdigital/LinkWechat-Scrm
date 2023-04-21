package com.linkwechat.common.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工管理范围表
 * @TableName sys_user_manage_scop
 */
@TableName(value ="sys_user_manage_scop")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserManageScop extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 员工主键id
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;



}