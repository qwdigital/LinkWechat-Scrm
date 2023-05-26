package com.linkwechat.domain.system.user.vo;

import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @date 2022年11月29日 12:15
 */
@Data
@ApiModel
public class SysUserVo {
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "主键id")
    private Long userId;

    /**
     * 企微员工ID
     */
    @ApiModelProperty(value = "企微员工ID")
    private String weUserId;

    /**
     * 对应mainDepartment
     */
    @ApiModelProperty(value = "对应mainDepartment")
    private Long deptId;

    /**
     * 员工账号
     */
    @ApiModelProperty(value = "员工账号")
    private String userName;


    /**
     * 职务信息，与岗位数据相匹配
     */
    @ApiModelProperty(value = "职务信息")
    private String position;


    /**
     * 用户性别
     */
    @ApiModelProperty(value = "用户性别, 0=男,1=女,2=未知")
    private String sex;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String avatar;

    /**
     * 用户头像缩略图
     */
    @ApiModelProperty(value = "用户头像缩略图")
    private String thumbAvatar;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    /**
     * 企微用户激活状态(1=已激活，2=已禁用，4=未激活，5=退出企业)
     */
    @ApiModelProperty(value = "企微用户激活状态(1=已激活，2=已禁用，4=未激活，5=退出企业)")
    private String weUserStatus;

    /**
     * open_userid
     */
    @ApiModelProperty(value = "open_userid")
    private String openUserid;


    /**
     * 用户类型（00管理员）(企微用户类型 1.创建者 2.内部系统管理员 3.外部系统管理员 4.分级管理员 5.成员)
     */
    @ApiModelProperty(value = "用户类型")
    private String userType;

    /**
     * 客服接待状态。1:接待中,2:停止接待
     */
    @ApiModelProperty(value = "接待状态。1:接待中,2:停止接待")
    private Integer kfStatus;

    @ApiModelProperty(value = "所属部门")
    private List<SysDeptVo> deptList;
}
