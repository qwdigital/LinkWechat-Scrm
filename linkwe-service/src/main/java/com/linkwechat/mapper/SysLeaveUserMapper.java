package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.domain.SysLeaveUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysLeaveUserMapper extends BaseMapper<SysLeaveUser> {
    void batchAddOrUpdate(@Param("leaveUsers") List<SysLeaveUser> leaveUsers);

    void leaveSysUser(@Param("weUserIds") List<String> weUserIds);

    SysUser findSysUserByWeUserId(String weUserId);
}
