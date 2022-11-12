package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.dto.SysUserDTO;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.fegin.QwSysUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author leejoker
 * @date 2022/4/29 22:59
 */
@Component
@Slf4j
public class QwSysUserFallbackFactory implements QwSysUserClient {
    @Override
    public AjaxResult<List<SysUser>> listAll() {
        return null;
    }

    @Override
    public AjaxResult syncUserAndDeptHandler(String msg) {
        return null;
    }

    @Override
    public AjaxResult callBackRemove(String corpId, String[] userIds) {
        return null;
    }


    @Override
    public AjaxResult add(SysUserDTO sysUser) {
        return null;
    }

    @Override
    public AjaxResult edit(SysUserDTO sysUser) {
        return null;
    }

    @Override
    public AjaxResult getUserInfoById(Long userId) {
        return null;
    }

    @Override
    public AjaxResult<List<SysUser>> list(SysUser sysUser) {
        return null;
    }

    @Override
    public AjaxResult<List<SysUser>> findAllSysUser(@RequestParam("weUserIds") String weUserIds,
                                                    @RequestParam("positions") String positions,@RequestParam("deptIds") String deptIds) {
        return null;
    }

}
