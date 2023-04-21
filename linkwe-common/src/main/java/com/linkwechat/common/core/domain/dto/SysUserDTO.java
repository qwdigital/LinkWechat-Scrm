package com.linkwechat.common.core.domain.dto;

import com.linkwechat.common.core.domain.SysUserManageScop;
import com.linkwechat.common.core.domain.entity.SysUser;
import lombok.Data;

import java.util.List;

/**
 * @author leejoker
 * @date 2022/5/25 21:10
 */
@Data
public class SysUserDTO extends SysUser {
    private String corpId;


    //员工数据权限
    private List<SysUserManageScop> sysUserManageScops;
}
