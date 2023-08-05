package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import com.linkwechat.fallback.QwSysDeptFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.linkwechat.common.core.domain.dto.SysUserDTO;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.fallback.QwSysUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author leejoker
 * @date 2022/4/29 22:58
 */
@FeignClient(value = "${wecom.serve.linkwe-auth}", fallback = QwSysUserFallbackFactory.class, contextId = "linkwe-auth-user")
public interface QwSysDeptClient {

    @GetMapping("/system/dept/findSysDeptByIds")
    AjaxResult<List<SysDept>> findSysDeptByIds(@RequestParam(value = "deptIds")String deptIds);

    @PostMapping("/system/dept/getListByDeptIds")
    AjaxResult<List<SysDeptVo>> getListByDeptIds(@RequestBody SysDeptQuery query);

    /**
     * 回调新增部门
     * @param query
     * @return
     */
    @PostMapping("/system/dept/callback/add")
    AjaxResult addDept(@RequestBody SysDeptQuery query);

    /**
     * 回调删除部门
     * @param query
     * @return
     */
    @PostMapping("/system/dept/callback/delete")
    AjaxResult delDept(@RequestBody SysDeptQuery query);

    /**
     * 回调更新部门
     * @param query
     * @return
     */
    @PostMapping("/system/dept/callback/update")
    AjaxResult updateDept(@RequestBody SysDeptQuery query);
}
