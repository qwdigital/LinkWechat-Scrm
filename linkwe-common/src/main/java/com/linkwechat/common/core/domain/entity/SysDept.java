package com.linkwechat.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门表 sys_dept
 *
 * @author ruoyi
 */
@Data
@TableName(value = "sys_dept")
public class SysDept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.INPUT)
    @TableField("dept_id")
    private Long deptId;

    /**
     * 父部门ID
     */
    @ApiModelProperty(value = "父部门id")
    @TableField("parent_id")
    private Long parentId;

    /**
     * 祖级列表
     */
    @ApiModelProperty(value = "祖级列表")
    @TableField("ancestors")
    private String ancestors;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    @TableField("dept_name")
    private String deptName;

    /**
     * 部门英文名称
     */
    @ApiModelProperty(value = "部门英文名称")
    @TableField("dept_en_name")
    private String deptEnName;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    @TableField("order_num")
    private Long orderNum;

    /**
     * 负责人
     */
    @ApiModelProperty(value = "负责人")
    @TableField("leader")
    private String leader;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

    /**
     * 部门状态:0正常,1停用
     */
    @ApiModelProperty(value = "部门状态:0正常,1停用")
    @TableField("status")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableField("del_flag")
    private String delFlag;

    /**
     * 父部门名称
     */
    @TableField(exist = false)
    private String parentName;


    /**
     * 子部门
     */
    @TableField(exist = false)
    private List<SysDept> children = new ArrayList<SysDept>();

    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 32, message = "部门名称长度不能超过32个字符")
    public String getDeptName() {
        return deptName;
    }

    @Size(min = 0, max = 32, message = "部门英文名称长度不能超过32个字符")
    public String getDeptEnName() {
        return deptEnName;
    }

    @NotBlank(message = "显示顺序不能为空")
    public Long getOrderNum() {
        return orderNum;
    }

    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    public String getPhone() {
        return phone;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("deptId", getDeptId())
                .append("parentId", getParentId()).append("ancestors", getAncestors()).append("deptName", getDeptName())
                .append("orderNum", getOrderNum()).append("leader", getLeader()).append("phone", getPhone())
                .append("email", getEmail()).append("status", getStatus()).append("delFlag", getDelFlag())
                .append("createBy", getCreateBy()).append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).toString();
    }
}
