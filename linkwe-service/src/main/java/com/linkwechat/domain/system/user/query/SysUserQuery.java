package com.linkwechat.domain.system.user.query;

import com.linkwechat.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author danmo
 * @date 2022年11月29日 12:13
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserQuery {

    private String corpId;

    @ApiModelProperty("员工ID")
    private Long userId;

    @ApiModelProperty("企微员工ID")
    private String weUserId;

    @ApiModelProperty("员工ID")
    private List<Long> userIds;


    @ApiModelProperty("员工名称")
    private String userName;


    @ApiModelProperty("企微员工ID")
    private List<String> weUserIds;

    @ApiModelProperty("部门ID")
    private List<Long> deptIds;

    @ApiModelProperty("角色ID")
    private List<Long> roleIds;

    @ApiModelProperty("职位")
    private List<String> positions;

    @ApiModelProperty("员工状态 1=已激活，2=已禁用，4=未激活，5=退出企业")
    private Integer weUserStatus;

    @ApiModelProperty("职状态员工，数据分配状态:0:未分配;1:已分配")
    private Integer isAllocate;

    @ApiModelProperty("是否开启会话存档 0-未开启 1-开启")
    private Integer isOpenChat;

    @ApiModelProperty("客服接待状态。1:接待中,2:停止接待")
    private Integer kfStatus;

    @ApiModelProperty("性别（0男 1女 2未知）")
    private Integer sex;


    private Integer isUserLeave;

    private List<SysUser> sysUsers;

}
