package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import com.linkwechat.fallback.QwSysDeptFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author leejoker
 * @date 2022/4/29 22:58
 */
@FeignClient(value = "${wecom.serve.linkwe-auth}", fallback = QwSysDeptFallbackFactory.class, contextId = "linkwe-auth-user")
public interface QwSysDeptClient {

    @GetMapping("/system/dept/findSysDeptByIds")
    AjaxResult<List<SysDept>> findSysDeptByIds(@RequestParam(value = "deptIds") String deptIds);

    @PostMapping("/system/dept/getListByDeptIds")
    AjaxResult<List<SysDeptVo>> getListByDeptIds(@RequestBody SysDeptQuery query);

    /**
     * 回调新增部门
     *
     * @param query
     * @return
     */
    @PostMapping("/system/dept/callback/add")
    AjaxResult addDept(@RequestBody SysDeptQuery query);

    /**
     * 回调删除部门
     *
     * @param query
     * @return
     */
    @PostMapping("/system/dept/callback/delete")
    AjaxResult delDept(@RequestBody SysDeptQuery query);

    /**
     * 回调更新部门
     *
     * @param query
     * @return
     */
    @PostMapping("/system/dept/callback/update")
    AjaxResult updateDept(@RequestBody SysDeptQuery query);

    /**
     * 获取部门数据，不做数据权限过滤
     *
     * @param query 查询条件
     * @return {@link AjaxResult<List<SysDept>>}
     * @author WangYX
     * @date 2023/07/17 10:06
     */
    @PostMapping("/system/dept/list/without/permission")
    AjaxResult<List<SysDept>> getListWithOutPermission(@RequestBody SysDept query);
}
