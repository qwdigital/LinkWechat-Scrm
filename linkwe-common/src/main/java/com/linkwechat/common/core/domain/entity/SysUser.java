package com.linkwechat.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.annotation.Excel.ColumnType;
import com.linkwechat.common.annotation.Excel.Type;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Excel(name = "用户序号", cellType = ColumnType.NUMERIC, prompt = "用户编号")
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("user_id")
    private Long userId;

    /**
     * 当前微信用户对应微信端的id
     */
    @ApiModelProperty(value = "当前微信用户对应微信端的id")
    @TableField("we_user_id")
    private String weUserId;

    /**
     * 对应mainDepartment
     */
    @ApiModelProperty(value = "对应mainDepartment")
    @TableField("dept_id")
    private Long deptId;


    /**
     * 对应mainDepartmentName
     */
    @ApiModelProperty(value = "对应mainDepartmentName")
    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private SysDept dept;

    /**
     * 用户账号
     */
    @Excel(name = "登录名称")
    @ApiModelProperty(value = "用户账号")
    @TableField("user_name")
    private String userName;

    /**
     * 用户部门关联信息
     */
    @TableField(exist = false)
    private List<SysUserDept> userDepts;

    /**
     * 职务信息，与岗位数据相匹配
     */
    @ApiModelProperty(value = "职务信息")
    @TableField("position")
    private String position;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    @ApiModelProperty(value = "手机号码")
    @TableField("phone_number")
    private String phoneNumber;


    /**
     * 用户性别
     */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    @ApiModelProperty(value = "用户性别, 0=男,1=女,2=未知")
    @TableField("sex")
    private String sex;

    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱")
    @ApiModelProperty(value = "用户邮箱")
    @TableField("email")
    private String email;

    /**
     * 企业邮箱
     */
    @Excel(name = "企业邮箱")
    @ApiModelProperty(value = "企业邮箱")
    @TableField("biz_mail")
    private String bizMail;

    /**
     * 直属上级
     */
    @ApiModelProperty(value = "直属上级")
    @TableField("leader")
    private String leader;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    @TableField("avatar")
    private String avatar;

    /**
     * 用户头像缩略图
     */
    @ApiModelProperty(value = "用户头像缩略图")
    @TableField("thumb_avatar")
    private String thumbAvatar;

    /**
     * 座机号码
     */
    @ApiModelProperty(value = "座机号码")
    @TableField("telephone")
    private String telephone;

    /**
     * 用户昵称
     */
    @Excel(name = "用户名称")
    @ApiModelProperty(value = "用户昵称")
    @TableField("nick_name")
    private String nickName;

    /**
     * 扩展属性
     */
    @ApiModelProperty(value = "扩展属性")
    @TableField("ext_attr")
    private String extAttr;

    /**
     * 企微用户激活状态(1=已激活，2=已禁用，4=未激活，5=退出企业)
     */
    @ApiModelProperty(value = "企微用户激活状态(1=已激活，2=已禁用，4=未激活，5=退出企业)")
    @TableField("we_user_status")
    private Integer weUserStatus;

    /**
     * 员工个人二维码
     */
    @ApiModelProperty(value = "员工个人二维码")
    @TableField("qr_code")
    private String qrCode;

    /**
     * 成员对外属性
     */
    @ApiModelProperty(value = "成员对外属性")
    @TableField("external_profile")
    private String externalProfile;

    /**
     * 对外职务
     */
    @ApiModelProperty(value = "对外职务")
    @TableField("external_position")
    private String externalPosition;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    @TableField("address")
    private String address;

    /**
     * open_userid
     */
    @ApiModelProperty(value = "open_userid")
    @TableField("open_userid")
    private String openUserid;

    /**
     * 客服接待状态。1:接待中,2:停止接待
     */
    @ApiModelProperty(value = "接待状态。1:接待中,2:停止接待")
    @TableField("kf_status")
    private Integer kfStatus;


    /**
     * 数据范围(1:全部数据权限 2:自定义数据权限 3:本部门数据权限 4:本部门及以下数据权限)
     */
    private Integer dataScope;


    /**
     * 工号
     */
    private String jobNumber;


    /**
     * 用户类型（00管理员）(企微用户类型 1.创建者 2.内部系统管理员 3.外部系统管理员 4.分级管理员 5.成员)
     */
    @ApiModelProperty(value = "用户类型")
    @TableField("user_type")
    private String userType;


    /**
     * 离职状态员工，数据分配状态:0:未分配;1:已分配
     */
    private Integer isAllocate;


    /**
     * 是否离职1:是；0:否
     */
    private Integer isUserLeave;


    /**
     * 离职时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dimissionTime;


    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 盐加密
     */
    @TableField(exist = false)
    private String salt;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    @ApiModelProperty(value = "帐号状态")
    @TableField("status")
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableField("del_flag")
    private Integer delFlag;

    /**
     * 最后登陆IP
     */
    @Excel(name = "最后登陆IP", type = Type.EXPORT)
    @TableField("login_ip")
    private String loginIp;

    /**
     * 最后登陆时间
     */
    @Excel(name = "最后登陆时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    @TableField("login_date")
    private Date loginDate;

    /**
     * 是否开启会话存档 0-未开启 1-开启
     */
    private Integer isOpenChat;

    /**
     * 角色对象
     */
    @TableField(exist = false)
    private List<SysRole> roles;

    /**
     * 角色组
     */
    @TableField(exist = false)
    private Long[] roleIds;

    /**
     * 角色检索id
     */
    @TableField(exist = false)
    private Long roleId;

    /**
     * 岗位组
     */
    @TableField(exist = false)
    private Long[] postIds;

    @TableField(exist = false)
    private String companyName;

    /**
     * 员工所在部门id多个使用逗号隔开
     */
    @TableField(exist = false)
    private String deptIds;

    /**
     * 是否开启动态日报 0开启，1关闭 默认开启0
     */
    @TableField("is_open_daily")
    private Integer openDaily;

    /**
     * 是否按照跟节点查询所有数据来查询，默认不是
     */
    @TableField(exist = false)
    private boolean checkIsRoot=false;


    public SysUser(Long userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    public String getNickName() {
        return nickName;
    }

    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    public String getUserName() {
        return userName;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    @Email(message = "企业邮箱格式不正确")
    @Size(min = 0, max = 50, message = "企业邮箱长度不能超过50个字符")
    public String getBizMail() {
        return bizMail;
    }

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonIgnore
    @JsonProperty
    public String getPassword() {
        return password;
    }

    public boolean isCheckIsRoot() {
        return checkIsRoot;
    }

    public void setCheckIsRoot(boolean checkIsRoot) {
        this.checkIsRoot = checkIsRoot;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("userId", getUserId())
                .append("userName", getUserName()).append("nickName", getNickName()).append("email", getEmail())
                .append("bizMail", getBizMail()).append("phonenumber", getPhoneNumber()).append("sex", getSex())
                .append("avatar", getAvatar()).append("password", getPassword()).append("salt", getSalt())
                .append("status", getStatus()).append("delFlag", getDelFlag()).append("loginIp", getLoginIp())
                .append("loginDate", getLoginDate()).append("createBy", getCreateBy())
                .append("createTime", getCreateTime()).append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime()).append("remark", getRemark()).toString();
    }
}
