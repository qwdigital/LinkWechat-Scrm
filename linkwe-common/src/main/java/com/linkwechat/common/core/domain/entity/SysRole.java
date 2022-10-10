package com.linkwechat.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.annotation.Excel.ColumnType;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 角色表 sys_role
 *
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("sys_role")
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    public SysRole(Long roleId) {
        this.roleId = roleId;
    }

    public SysRole(String roleKey) {
        this.roleKey = roleKey;
    }

    /**
     * 角色ID
     */
    @Excel(name = "角色序号", cellType = ColumnType.NUMERIC)
    @TableId(type = IdType.AUTO)
    @TableField("role_id")
    private Long roleId;

    /**
     * 角色名称
     */
    @Excel(name = "角色名称")
    @TableField("role_name")
    private String roleName;

    /**
     * 角色权限
     */
    @Excel(name = "角色权限")
    @TableField("role_key")
    private String roleKey;

    /**
     * 角色排序
     */
    @Excel(name = "角色排序")
    @TableField("role_sort")
    private String roleSort;

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限）
     */
    @Excel(name = "数据范围", readConverterExp = "1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限")
    @TableField("data_scope")
    private String dataScope;

    /**
     * 角色状态（0正常 1停用）
     */
    @Excel(name = "角色状态", readConverterExp = "0=正常,1=停用")
    @TableField("status")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableField("del_flag")
    private String delFlag;

    /**
     * 菜单组
     */
    @TableField(exist = false)
    private Long[] menuIds;

    /**
     * 部门组（数据权限）
     */
    @TableField(exist = false)
    private Long[] deptIds;

    @TableField("base_role")
    private Integer baseRole;

    @TableField(exist = false)
    private Long[] users;

    @TableField(exist = false)
    private Long oldRoleId;

    public boolean isAdmin() {
        return isAdmin(this.roleId);
    }

    public boolean isAdmin(Long roleId) {
        return roleId != null && "SUPPER_ADMIN".equals(getRoleKey());
    }

    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    public String getRoleName() {
        return roleName;
    }

    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    public String getRoleKey() {
        return roleKey;
    }

    @NotBlank(message = "显示顺序不能为空")
    public String getRoleSort() {
        return roleSort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("roleId", getRoleId())
                .append("roleName", getRoleName()).append("roleKey", getRoleKey()).append("roleSort", getRoleSort())
                .append("dataScope", getDataScope()).append("status", getStatus()).append("delFlag", getDelFlag())
                .append("createBy", getCreateBy()).append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("remark", getRemark())
                .append("baseRole", getBaseRole()).toString();
    }
}
