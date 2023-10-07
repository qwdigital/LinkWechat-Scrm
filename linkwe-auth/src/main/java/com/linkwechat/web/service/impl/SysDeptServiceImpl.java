package com.linkwechat.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.constant.UserConstants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.TreeSelect;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.text.Convert;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.wecom.entity.department.WeDeptEntity;
import com.linkwechat.domain.wecom.query.department.WeDeptQuery;
import com.linkwechat.domain.wecom.vo.department.WeDeptInfoVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptVo;
import com.linkwechat.fegin.QwDeptClient;
import com.linkwechat.web.mapper.SysDeptMapper;
import com.linkwechat.web.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门管理 服务实现
 *
 * @author ruoyi
 */
@Slf4j
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {
    @Resource
    private SysDeptMapper deptMapper;

    @Resource
    private QwDeptClient deptClient;

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    @Override
    public List<SysDept> selectUserDeptList(String openUserid) {
        return deptMapper.selectUserDeptList(openUserid);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysDept dept : depts) {
            tempList.add(dept.getDeptId());
        }
        for (Iterator<SysDept> iterator = depts.iterator(); iterator.hasNext(); ) {
            SysDept dept = (SysDept) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Integer> selectDeptListByRoleId(Long roleId) {
        return deptMapper.selectDeptListByRoleId(roleId);
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(Long deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(SysDept dept) {
        Long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public void checkDeptDataScope(Long deptId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            SysDept dept = new SysDept();
            dept.setDeptId(deptId);
            List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
            if (StringUtils.isEmpty(depts))
            {
                throw new WeComException("没有权限访问部门数据！");
            }
        }
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept) {
        SysDept info = deptMapper.selectDeptById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new CustomException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        return deptMapper.insertDept(dept);
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysDept dept)
    {
        SysDept newParentDept = deptMapper.selectDeptById(dept.getParentId());
        SysDept oldDept = deptMapper.selectDeptById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept))
        {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors()))
        {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept)
    {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        deptMapper.updateDeptStatusNormal(deptIds);
    }


    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(Long deptId) {
        return deptMapper.deleteDeptById(deptId);
    }


    @Override
    public List<SysDept> syncWeDepartment(String corpId) {
        WeDeptQuery query = new WeDeptQuery();
        query.setCorpid(corpId);
        WeDeptVo weDeptVo = deptClient.getDeptList(query).getData();
        if(Objects.isNull(weDeptVo)){
            log.error("拉取企微部门接口失败 query：{}",query.getCorpid());
            return new ArrayList<>();
        }
        List<SysDept> sysDeptList = weDeptVo.getDepartment().stream().map(dept -> {
            LoginUser user = SecurityUtils.getLoginUser();
            SysDept d = new SysDept();
            d.setDeptId(dept.getId());
            d.setParentId(dept.getParentId());
            //设置ancestors
            TreeSet<Long> parentIds = new TreeSet<>();
            getParentDeptIds(weDeptVo.getDepartment(),dept.getId(),parentIds);
            d.setAncestors(parentIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
            d.setDeptName(dept.getName());
            d.setDeptEnName(dept.getNameEn());
            d.setLeader(String.join(",", dept.getDepartmentLeader()));
            d.setOrderNum(dept.getOrder());
            return d;
        }).collect(Collectors.toList());
        //不存在的移除
        if(CollectionUtil.isNotEmpty(sysDeptList)){
            this.remove(
                    new LambdaQueryWrapper<SysDept>()
                            .notIn(SysDept::getDeptId,sysDeptList.stream().map(SysDept::getDeptId).collect(Collectors.toList())
                            ));
        }
        saveOrUpdateBatch(sysDeptList,200);
        return sysDeptList;
    }

    /**
     * 递归获取父级ids
     * @param weDeptEntityList 列表
     * @param currentId 当前节点
     * @param parentIds 父节点ID列表
     */
    private static void getParentDeptIds(List<WeDeptEntity> weDeptEntityList, Long currentId, TreeSet<Long> parentIds){
        for (WeDeptEntity weDept : weDeptEntityList) {

            if(Objects.equals(0L,currentId)){
                return;
            }
            //判断是否有父节点
            if (currentId.equals(weDept.getId())) {
                parentIds.add(weDept.getParentId());
                getParentDeptIds(weDeptEntityList, weDept.getParentId(), parentIds);
            }
        }
    }

    @Override
    public List<SysDept> findSysDeptByIds(List<String> deptIds) {
        return this.baseMapper.selectList(new LambdaQueryWrapper<SysDept>()
                .in(SysDept::getDeptId,deptIds));
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<SysDept> it = childList.iterator();
                while (it.hasNext()) {
                    SysDept n = (SysDept) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext()) {
            SysDept n = (SysDept) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }


    @Override
    public List<SysDeptVo> getListByDeptIds(SysDeptQuery query) {
        List<SysDept> deptList = list(new LambdaQueryWrapper<SysDept>().in(SysDept::getDeptId, query.getDeptIds()).eq(SysDept::getDelFlag, 0));
        if (CollectionUtil.isNotEmpty(deptList)) {
            return deptList.stream().map(item -> {
                SysDeptVo sysDeptVo = new SysDeptVo();
                BeanUtil.copyProperties(item, sysDeptVo);
                return sysDeptVo;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void callbackAdd(SysDeptQuery query) {
        List<Long> deptIds = query.getDeptIds();
        if(CollectionUtil.isNotEmpty(deptIds)){
            WeDeptQuery weDeptQuery = new WeDeptQuery();
            weDeptQuery.setId(query.getDeptIds().get(0));
            WeDeptInfoVo deptInfo = deptClient.getDeptDetail(weDeptQuery).getData();

            if(Objects.nonNull(deptInfo)){
                WeDeptEntity department = deptInfo.getDepartment();
                SysDept parentInfo = deptMapper.selectDeptById(department.getParentId());
                SysDept dept = new SysDept();
                dept.setDeptId(department.getId());
                dept.setParentId(department.getParentId());
                //设置ancestors
                if(Objects.nonNull(parentInfo)){
                    dept.setAncestors(parentInfo.getAncestors() + "," + dept.getParentId());
                }else {
                    dept.setAncestors(dept.getParentId() + "");
                }
                dept.setDeptName(department.getName());
                dept.setDeptEnName(department.getNameEn());
                dept.setLeader(String.join(",", department.getDepartmentLeader()));
                dept.setOrderNum(department.getOrder());
                saveOrUpdate(dept);
            }
        }

    }

    @Override
    public void callbackDelete(SysDeptQuery query) {
        List<Long> deptIds = query.getDeptIds();
        if(CollectionUtil.isNotEmpty(deptIds)){
            update(new LambdaUpdateWrapper<SysDept>().set(SysDept::getDelFlag,1).in(SysDept::getDeptId,deptIds));
        }
    }

    @Override
    public void callbackUpdate(SysDeptQuery query) {
        callbackAdd(query);
    }

    @Override
    public boolean isRoot(Long deptId) {
        if(deptId != null){
            SysDept sysDept = this.getById(deptId);
            if(null != sysDept && new Long(0).equals(sysDept.getParentId())){
                return true;
            }
            return false;
        }
        return false;
    }
}
