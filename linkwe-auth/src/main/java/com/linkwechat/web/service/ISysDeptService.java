package com.linkwechat.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.TreeSelect;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author ruoyi
 */
public interface ISysDeptService extends IService<SysDept> {
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);


    /**
     * 根据用户id获取部门
     * @param openUserid
     * @return
     */
    public List<SysDept> selectUserDeptList(String openUserid);

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    public List<SysDept> buildDeptTree(List<SysDept> depts);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    public List<Integer> selectDeptListByRoleId(Long roleId);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public SysDept selectDeptById(Long deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(Long deptId);

    /**
     * 是否存在部门子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public boolean hasChildByDeptId(Long deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkDeptExistUser(Long deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    public String checkDeptNameUnique(SysDept dept);

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    public void checkDeptDataScope(Long deptId);

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept);

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(Long deptId);

    /**
     * 同步部门
     */
    public List<SysDept> syncWeDepartment(String corpId);


    /**
     * 根据部门id批量获取部门
     * @param deptIds
     * @return
     */
    List<SysDept> findSysDeptByIds(List<String> deptIds);

    /**
     * 根据部门ID获取部门列表
     * @param query
     * @return
     */
    public List<SysDeptVo> getListByDeptIds(SysDeptQuery query);

    /**
     * 回调新增部门
     * @param query
     */
    void callbackAdd(SysDeptQuery query);

    /**
     * 回调删除部门
     * @param query
     */
    void callbackDelete(SysDeptQuery query);

    /**
     * 回调更新部门
     * @param query
     */
    void callbackUpdate(SysDeptQuery query);

    /**
     * 判断当前节点是不是跟节点
     * @param deptId
     * @return
     */
    boolean isRoot(Long deptId);
}
