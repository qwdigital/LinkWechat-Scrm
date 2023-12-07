package com.linkwechat.fallback;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.dto.SysUserDTO;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwSysUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import org.springframework.web.bind.annotation.*;
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
    public AjaxResult syncUserHandler(JSONObject msg) {
        return null;
    }

    @Override
    public AjaxResult delete(List<String> weUserIds) {
        return null;
    }

    @Override
    public AjaxResult add(SysUserQuery query) {
        return null;
    }

    @Override
    public AjaxResult edit(SysUserQuery query) {
        return null;
    }

    @Override
    public AjaxResult getUserInfoById(Long userId) {
        return null;
    }

//    @Override
//    public AjaxResult<List<SysUser>> list(SysUser sysUser) {
//        return null;
//    }

    @Override
    public AjaxResult<SysUser> getInfo(String weUserId) {
        return null;
    }

    @Override
    public AjaxResult<List<SysUser>> findAllSysUser(String weUserIds, String positions, String deptIds) {
        return null;
    }

    @Override
    public AjaxResult<SysUser> findOrSynchSysUser(String weuserId) {
        return null;
    }

    @Override
    public AjaxResult updateUserKfStatus(SysUser sysUser) {
        return null;
    }

    @Override
    public AjaxResult updateUserChatStatus(SysUserQuery query) {
        return null;
    }

    @Override
    public AjaxResult<List<SysUserVo>> getUserListByWeUserIds(SysUserQuery query) {
        return null;
    }

    @Override
    public AjaxResult<List<SysUser>> findSysUser(SysUserQuery query) {
        return null;
    }

    @Override
    public AjaxResult<List<String>> screenConditWeUser(String weUserIds, String deptIds, String positions) {
        return null;
    }

    @Override
    public AjaxResult builderLeaveSysUser(SysUserQuery sysUsers) {
        return null;
    }

    @Override
    public AjaxResult<List<SysUser>> listByQuery(SysUser sysUser) {
        return null;
    }
}
