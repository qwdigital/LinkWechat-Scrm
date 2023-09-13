package com.linkwechat.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.aop.DataScopeAspect;
import com.linkwechat.common.constant.UserConstants;
import com.linkwechat.common.core.domain.dto.SysUserDTO;
import com.linkwechat.common.core.domain.entity.SysRole;
import com.linkwechat.common.enums.RoleType;
import com.linkwechat.common.enums.UserTypes;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.Arith;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.web.domain.SysRoleDept;
import com.linkwechat.web.domain.SysRoleMenu;
import com.linkwechat.web.domain.vo.RoleDetailVo;
import com.linkwechat.web.domain.vo.RoleVo;
import com.linkwechat.web.mapper.*;
import com.linkwechat.web.service.ISysRoleService;
import com.linkwechat.web.service.ISysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysRoleMenuMapper roleMenuMapper;

    @Resource
    private SysMenuMapper menuMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Resource
    private SysRoleDeptMapper roleDeptMapper;

    @Resource
    @Lazy
    private ISysUserService iSysUserService;

    public List<SysRole> selectSuperAdminBaseRoleList() {
        List<SysRole> roleList = roleMapper.selectSuperAdminBaseRoleList();
        return roleList.stream().peek(role -> {
            role.setOldRoleId(role.getRoleId());
            role.setRoleId(null);
        }).collect(Collectors.toList());
    }


    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role);
    }

    @Override
    public List<RoleVo> selectRoleBaseList(SysRole role) {
        List<RoleVo> roleVos=roleMapper.selectRoleBaseList(role);;

        if(CollectionUtils.isNotEmpty(roleVos)){
            //超级管理员查看所有角色
            if(UserTypes.USER_TYPE_SUPPER_ADMIN.getSysRoleKey().equals(SecurityUtils.getUserType())){//超级管理员
                return  roleVos;
            }else if(UserTypes.USER_TYPE_FJ_ADMIN.getSysRoleKey().equals(SecurityUtils.getUserType())){//分级管理员(过滤超管与分管)
                roleVos=roleVos.stream().filter(item->!item.getRoleKey().equals(RoleType.WECOME_USER_TYPE_VJZ.getSysRoleKey())
                        && !item.getRoleKey().equals(RoleType.WECOME_USER_TYPE_FJGLY.getSysRoleKey())).collect(Collectors.toList());
            }else{//普通成员以及自定义角色(过滤超管与分管与普通成员)
                roleVos=roleVos.stream().filter(item->!item.getRoleKey().equals(RoleType.WECOME_USER_TYPE_VJZ.getSysRoleKey())
                        && !item.getRoleKey().equals(RoleType.WECOME_USER_TYPE_FJGLY.getSysRoleKey())
                        && !item.getRoleKey().equals(RoleType.WECOME_USER_TYPE_CY.getSysRoleKey())
                ).collect(Collectors.toList());
            }
        }
        return roleVos;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public List<SysRole> selectRolesPermissionByUserId(Long userId) {
        return roleMapper.selectRolePermissionByUserId(userId);
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return selectRoleList(new SysRole());
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleIdsByUserId(Long userId) {
        return roleMapper.selectRoleListByUserId(userId).stream().map(SysRole::getRoleId).collect(Collectors.toList());
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<SysRole> selectRoleListByUserId(Long userId) {
        return roleMapper.selectRoleListByUserId(userId);
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public RoleDetailVo selectRoleById(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        RoleDetailVo roleDetail = BeanUtil.copyProperties(role, RoleDetailVo.class);
        roleDetail.setUsers(userRoleMapper.selectUserByRoleId(roleId));
//        if (DataScopeAspect.DATA_SCOPE_CUSTOM.equals(role.getDataScope())) {
//            roleDetail.setDepts(roleDeptMapper.selectRoleDeptList(roleId));
//        }
        roleDetail.setMenus(menuMapper.selectMenuListByRoleId(roleId));
        return roleDetail;
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new CustomException("不允许操作超级管理员角色");
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(SysRole role) {
        // 新增角色信息
        roleMapper.insertRole(role);
        if (CollectionUtils.isNotEmpty(Arrays.asList(role.getUsers()))) {
            insertRoleUser(role.getRoleId(), role.getUsers());
        }
        insertRoleDept(role);
        return insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(SysRole role) {
        authDataScope(role);
        userRoleMapper.deleteUserRoleByRoleId(role.getRoleId());
        if (CollectionUtils.isNotEmpty(Arrays.asList(role.getUsers()))) {
            insertRoleUser(role.getRoleId(), role.getUsers());
        }
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(SysRole role) {
        return roleMapper.updateRole(role);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role) {
        // 修改角色信息
        roleMapper.updateRole(role);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
        // 新增角色和部门信息（数据权限）
        return insertRoleDept(role);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public int insertRoleDept(SysRole role) {
        int rows = 1;
//        if (DataScopeAspect.DATA_SCOPE_CUSTOM.equals(role.getDataScope())) {
//            // 新增角色与部门（数据权限）管理
//            List<SysRoleDept> list = new ArrayList<SysRoleDept>();
//            if(role.getDeptIds() !=null ){
//                for (Long deptId : role.getDeptIds()) {
//                    SysRoleDept rd = new SysRoleDept();
//                    rd.setRoleId(role.getRoleId());
//                    rd.setDeptId(deptId);
//                    list.add(rd);
//                }
//
//            }
//            if (list.size() > 0) {
//                rows = roleDeptMapper.batchRoleDept(list);
//            }
//        }
        return rows;
    }

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int deleteRoleById(Long roleId) {


        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRole(roleId));
            SysRole role = getById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new CustomException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        return roleMapper.deleteRoleByIds(roleIds);
    }


    private void insertRoleUser(Long roleId, Long[] userIds) {
        List<SysUserDTO> sysUserDTOS=new ArrayList<>();

        Arrays.asList(userIds).stream().forEach(userId->{
            SysUserDTO sysUserDTO = new SysUserDTO();
            sysUserDTO.setUserId(userId);
            sysUserDTO.setRoleIds( Arrays.asList(roleId).stream().toArray(Long[]::new));
            sysUserDTOS.add(sysUserDTO);

        });
//        for (Long userId : userIds) {
//            SysUserDTO sysUserDTO = new SysUserDTO();
//            sysUserDTO.setUserId(userId);
//            sysUserDTO.setRoleIds( Arrays.asList(roleId).stream().toArray(Long[]::new));
//            iSysUserService.editUserRole(sysUserDTO);
//        }
        if(CollectionUtils.isNotEmpty(sysUserDTOS)){
            iSysUserService.batchEditUserRole(roleId,sysUserDTOS);
        }

    }
}
