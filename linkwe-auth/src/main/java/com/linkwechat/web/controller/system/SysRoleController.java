package com.linkwechat.web.controller.system;

import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.UserConstants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysRole;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.framework.service.TokenService;
import com.linkwechat.web.domain.vo.RoleVo;
import com.linkwechat.web.service.ISysRoleService;
import com.linkwechat.web.service.ISysUserService;
import com.linkwechat.web.service.SysPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/role")
@Api(tags = "角色管理")
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService roleService;


    @Autowired
    private LinkWeChatConfig linkWeChatConfig;



    //@PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    @ApiOperation(value = "获取角色列表")
    public TableDataInfo list(SysRole role) {
        startPage();
        List<RoleVo> list = roleService.selectRoleBaseList(role);
        return getDataTable(list);
    }

    //    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    //@PreAuthorize("@ss.hasPermi('system:role:export')")
    @GetMapping("/export")
    @ApiOperation(value = "导出角色列表")
    public AjaxResult export(SysRole role) {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        return util.exportExcel(list, "角色数据");
    }

    /**
     * 根据角色编号获取详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    @ApiOperation(value = "根据角色编号获取详细信息")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        return AjaxResult.success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    //@PreAuthorize("@ss.hasPermi('system:role:add')")
//    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增角色")
    public AjaxResult add(@Validated @RequestBody SysRole role) {
//        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
//            return AjaxResult.error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
//        }
        role.setRoleKey("USER_ROLE");
        role.setStatus("0");
        role.setCreateBy(SecurityUtils.getUserName());
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    ////@PreAuthorize("@ss.hasPermi('system:role:edit')")
//    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改角色")
    public AjaxResult edit(@Validated @RequestBody SysRole role) {
        if(linkWeChatConfig.isDemoEnviron()){

            return AjaxResult.error("当前演示环境角色不可修改");

        }
        roleService.checkRoleAllowed(role);
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        role.setUpdateBy(SecurityUtils.getUserName());

        if (roleService.updateRole(role) > 0) {
//            // 更新缓存用户权限
//            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
//            if (StringUtils.isNotNull(loginUser.getSysUser()) && !loginUser.getSysUser().isAdmin()) {
//                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getSysUser()));
//                loginUser.setSysUser(userService.selectUserByUserName(loginUser.getSysUser().getUserName()));
//                tokenService.setLoginUser(loginUser);
//            }
            return AjaxResult.success();
        }
        return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    /**
     * 修改保存数据权限
     */
    //@PreAuthorize("@ss.hasPermi('system:role:edit')")
//    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    @ApiOperation(value = "修改保存数据权限")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    //@PreAuthorize("@ss.hasPermi('system:role:edit')")
//    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    @ApiOperation(value = "状态修改")
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(SecurityUtils.getUserName());
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    //
//    @Log(title = "角色管理", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{roleIds}")
//    @ApiOperation(value = "删除角色")
//    public AjaxResult remove(@PathVariable Long[] roleIds) {
//        return toAjax(roleService.deleteRoleByIds(roleIds));
//    }


    @DeleteMapping("/{roleId}")
    @ApiOperation(value = "删除角色")
    public AjaxResult remove(@PathVariable Long roleId) {
        if (roleService.countUserRoleByRoleId(roleId) > 0) {
            return AjaxResult.error("当前角色已分配不可删除");
        }
        return toAjax(roleService.deleteRoleById(roleId));
    }

    /**
     * 获取角色选择框列表
     */
    //@PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    @ApiOperation(value = "获取角色选择框列表")
    public AjaxResult optionselect() {
        return AjaxResult.success(roleService.selectRoleAll());
    }
}
