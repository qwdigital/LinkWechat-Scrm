package com.linkwechat.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author leejoker
 * <p>
 * 用户部门关联信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user_dept")
public class SysUserDept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("user_dept_id")
    private Long userDeptId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户主键")
    @TableField("user_id")
    private Long userId;

    /**
     * 企微用户id
     */
    @ApiModelProperty(value = "企微用户id")
    @TableField("we_user_id")
    private String weUserId;

    /**
     * open_userid
     */
    @ApiModelProperty(value = "open_userid")
    @TableField("open_userid")
    private String openUserid;

    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    @TableField("dept_id")
    private Long deptId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    private String deptName;

    /**
     * 部门英文名称
     */
    @ApiModelProperty(value = "部门英文名称")
    @TableField(exist = false)
    private String deptEnName;

    /**
     * 部门内的排序值
     */
    @ApiModelProperty(value = "部门内的排序值")
    @TableField("order_in_dept")
    private String orderInDept;

    /**
     * 在所在的部门内是否为部门负责人，0-否；1-是
     */
    @ApiModelProperty(value = "在所在的部门内是否为部门负责人，0-否；1-是")
    @TableField("leader_in_dept")
    private Integer leaderInDept;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableField("del_flag")
    private String delFlag;
}
