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
    public List<WeDepartment> selectWeDepartmentList();

    /**
     * 新增企业微信组织架构相关
     * 
     * @param weDepartment 企业微信组织架构相关
     * @return 结果
     */
    public void insertWeDepartment(WeDepartment weDepartment);

    public int insertWeDepartmentNoToWeCom(WeDepartment weDepartment);

    /**
     * 修改企业微信组织架构相关
     * 
     * @param weDepartment 企业微信组织架构相关
     * @return 结果
     */
    public void updateWeDepartment(WeDepartment weDepartment);




    /**
     * 同步部门
     */
    public List<WeDepartment> synchWeDepartment();


    /**
     * 根据部门id删除部门
     * @param ids
     */
    public void deleteWeDepartmentByIds(String[] ids);
}
