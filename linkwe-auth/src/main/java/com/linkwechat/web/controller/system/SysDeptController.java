package com.linkwechat.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.UserConstants;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import com.linkwechat.web.mapper.SysUserMapper;
import com.linkwechat.web.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 部门信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/dept")
@Api(tags = "组织管理")
public class SysDeptController extends BaseController {
    @Autowired
    private ISysDeptService deptService;

    @Resource
    private SysUserMapper userMapper;

    /**
     * 获取部门列表
     */
    //@PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    @ApiOperation(value = "部门列表")
    public AjaxResult list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(depts);
    }


    /**
     * 根据部门id批量获取部门
     *
     * @param deptIds
     * @return
     */
    @GetMapping("/findSysDeptByIds")
    public AjaxResult<List<SysDept>> findSysDeptByIds(@RequestParam(value = "deptIds") String deptIds) {


        return AjaxResult.success(
                deptService.findSysDeptByIds(
                        Arrays.asList(deptIds.split(","))
                )
        );
    }

    /**
     * 查询部门列表（排除节点）
     */
    //@PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    @ApiOperation(value = "部门列表（排除节点）")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().intValue() == deptId || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","),
                    deptId + "")) {
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    @ApiOperation(value = "根据部门编号获取详细信息")
    public AjaxResult getInfo(@PathVariable Long deptId) {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    @ApiOperation(value = "获取部门下拉树列表")
    public AjaxResult treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 根据当前登录用户获取对应角色菜单树
     *
     * @return
     */
    @GetMapping("/roleDeptTreeselect")
    @ApiOperation(value = "获取部门下拉树列表")
    public AjaxResult roleDeptTreeselect() {
        SysUser user = userMapper.selectUserById(SecurityContextHolder.getUserId());
        List<SysDept> depts = deptService.selectUserDeptList(user.getOpenUserid());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    @ApiOperation(value = "加载对应角色部门列表树")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * 新增部门
     */
    //@PreAuthorize("@ss.hasPermi('system:dept:add')")
//    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增部门")
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUserName());
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    //@PreAuthorize("@ss.hasPermi('system:dept:edit')")
//    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改部门")
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        Long deptId = dept.getDeptId();
        deptService.checkDeptDataScope(deptId);
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(deptId)) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0) {
            return AjaxResult.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getUserName());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    //@PreAuthorize("@ss.hasPermi('system:dept:remove')")
//    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    @ApiOperation(value = "删除部门")
    public AjaxResult remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return AjaxResult.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.error("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * 根据部门ID获取部门列表
     */
    @PostMapping(value = "/getListByDeptIds")
    @ApiOperation(value = "根据部门ID获取部门列表")
    public AjaxResult<List<SysDeptVo>> getListByDeptIds(@Validated @RequestBody SysDeptQuery query) {
        return AjaxResult.success(deptService.getListByDeptIds(query));
    }

    @ApiOperation(value = "回调新增部门")
    @PostMapping("/callback/add")
    public AjaxResult callbackAdd(@RequestBody SysDeptQuery query) {
        deptService.callbackAdd(query);
        return AjaxResult.success();
    }

    @ApiOperation(value = "回调删除部门")
    @PostMapping("/callback/delete")
    public AjaxResult callbackDelete(@RequestBody SysDeptQuery query) {
        deptService.callbackDelete(query);
        return AjaxResult.success();
    }

    @ApiOperation(value = "回调更新部门")
    @PostMapping("/callback/update")
    public AjaxResult callbackUpdate(@RequestBody SysDeptQuery query) {
        deptService.callbackUpdate(query);
        return AjaxResult.success();
    }

    /**
     * 获取部门列表，不做数据权限校验
     *
     * @param
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/17 9:59
     */
    @PostMapping("/list/without/permission")
    @ApiOperation(value = "企业企微部门列表-不做数据权限校验")
    public AjaxResult getListWithOutPermission(@RequestBody SysDept dept) {
        LambdaQueryWrapper<SysDept> queryWrapper = Wrappers.lambdaQuery(SysDept.class);
        queryWrapper.eq(SysDept::getDelFlag, Constants.COMMON_STATE);
        //查询条件扩展
        queryWrapper.eq(dept.getDeptId() != null, SysDept::getDeptId, dept.getDeptId());
        List<SysDept> list = deptService.list(queryWrapper);
        return AjaxResult.success(list);
    }


}
