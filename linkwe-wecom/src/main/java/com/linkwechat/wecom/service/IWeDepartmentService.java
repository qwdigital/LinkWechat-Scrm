package com.linkwechat.wecom.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeDepartment;
import org.apache.ibatis.annotations.Param;

/**
 * 企业微信组织架构相关Service接口
 * 
 * @author ruoyi
 * @date 2020-09-01
 */
public interface IWeDepartmentService extends IService<WeDepartment>
{

    /**
     * 查询企业微信组织架构相关列表
     * 
     * @return 企业微信组织架构相关集合
     */
    public List<WeDepartment> getList();

    /**
     * 新增企业微信组织架构相关
     * 
     * @param weDepartment 企业微信组织架构相关
     * @return 结果
     */
    public void insert(WeDepartment weDepartment);

    /**
     * 新增部门
     * @param weDepartment
     * @return
     */
    public void insert2Data(WeDepartment weDepartment);

    /**
     * 修改企业微信组织架构相关
     * 
     * @param weDepartment 企业微信组织架构相关
     * @return 结果
     */
    public void update(WeDepartment weDepartment);




    /**
     * 同步部门
     */
    public List<WeDepartment> synchWeDepartment();


    /**
     * 根据部门id删除部门
     * @param ids
     */
    public void deleteByIds(Long[] ids);

    /**
     * 根据部门id 查询部门信息
     *
     * @param deptId 部门id
     * @return 企业微信组织架构相关集合
     */
    public WeDepartment getByDeptId(Long deptId);
}
