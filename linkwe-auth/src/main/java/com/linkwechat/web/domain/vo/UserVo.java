package com.linkwechat.web.domain.vo;

import com.linkwechat.common.annotation.PhoneEncryptField;
import com.linkwechat.common.core.domain.SysUserManageScop;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.common.core.domain.entity.SysUserDept;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/4/15 10:28
 */
@Data
public class UserVo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long userId;

    @ApiModelProperty(value = "当前微信用户对应微信端的id")
    private String weUserId;

    @ApiModelProperty(value = "用户账号")
    private String userName;

    @ApiModelProperty(value = "所属部门")
    private List<SysUserDept> userDepts;

    @ApiModelProperty(value = "职务信息")
    private String position;

    @PhoneEncryptField
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "open_userid")
    private String openUserid;

    @ApiModelProperty(value = "所属角色")
    private List<UserRoleVo> roles;

    @ApiModelProperty(value = "自定义数据范围")
    private List<SysDept> roleDepts;

    //数据范围(1:全部数据权限 2:自定义数据权限 3:本部门数据权限 4:本部门及以下数据权限 5:本人数据)
    private Integer dataScope;

    private String scopeDept;


    private List<SysUserManageScop> sysUserManageScops;

    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    public String getNickName() {
        return nickName;
    }

    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    public String getUserName() {
        return userName;
    }
}
