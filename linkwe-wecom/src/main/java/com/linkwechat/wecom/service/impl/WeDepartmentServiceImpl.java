package com.linkwechat.wecom.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeDepartmentMapper;
import com.linkwechat.wecom.domain.WeDepartment;
import com.linkwechat.wecom.service.IWeDepartmentService;

/**
 * 企业微信组织架构相关Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-09-01
 */
@Service
public class WeDepartmentServiceImpl implements IWeDepartmentService 
{
    @Autowired
    private WeDepartmentMapper weDepartmentMapper;

    /**
     * 查询企业微信组织架构相关
     * 
     * @param id 企业微信组织架构相关ID
     * @return 企业微信组织架构相关
     */
    @Override
    public WeDepartment selectWeDepartmentById(Long id)
    {
        return weDepartmentMapper.selectWeDepartmentById(id);
    }

    /**
     * 查询企业微信组织架构相关列表
     * 
     * @param weDepartment 企业微信组织架构相关
     * @return 企业微信组织架构相关
     */
    @Override
    public List<WeDepartment> selectWeDepartmentList(WeDepartment weDepartment)
    {
        return weDepartmentMapper.selectWeDepartmentList(weDepartment);
    }

    /**
     * 新增企业微信组织架构相关
     * 
     * @param weDepartment 企业微信组织架构相关
     * @return 结果
     */
    @Override
    public int insertWeDepartment(WeDepartment weDepartment)
    {
        return weDepartmentMapper.insertWeDepartment(weDepartment);
    }

    /**
     * 修改企业微信组织架构相关
     * 
     * @param weDepartment 企业微信组织架构相关
     * @return 结果
     */
    @Override
    public int updateWeDepartment(WeDepartment weDepartment)
    {
        return weDepartmentMapper.updateWeDepartment(weDepartment);
    }

    /**
     * 批量删除企业微信组织架构相关
     * 
     * @param ids 需要删除的企业微信组织架构相关ID
     * @return 结果
     */
    @Override
    public int deleteWeDepartmentByIds(Long[] ids)
    {
        return weDepartmentMapper.deleteWeDepartmentByIds(ids);
    }

    /**
     * 删除企业微信组织架构相关信息
     * 
     * @param id 企业微信组织架构相关ID
     * @return 结果
     */
    @Override
    public int deleteWeDepartmentById(Long id)
    {
        return weDepartmentMapper.deleteWeDepartmentById(id);
    }
}
