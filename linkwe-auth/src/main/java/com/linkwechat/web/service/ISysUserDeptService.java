package com.linkwechat.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.entity.SysUserDept;

import java.util.List;

/**
 * @author leejoker
 * @date 2022/3/17 2:01
 */
public interface ISysUserDeptService extends IService<SysUserDept> {
    void buildSysUserDept(List<SysUserDept> sysUserDeptList);
}
