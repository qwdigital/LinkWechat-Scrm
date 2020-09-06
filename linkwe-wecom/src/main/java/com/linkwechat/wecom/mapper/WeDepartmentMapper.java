package com.linkwechat.wecom.mapper;

import java.util.List;
import com.linkwechat.wecom.domain.WeDepartment;

/**
 * 企业微信组织架构相关Mapper接口
 * 
 * @author ruoyi
 * @date 2020-09-01
 */
public interface WeDepartmentMapper 
{
    /**
     * 查询企业微信组织架构相关
     * 
     * @param id 企业微信组织架构相关ID
     * @return 企业微信组织架构相关
     */
    public WeDepartment selectWeDepartmentById(Long id);

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
    public int insertWeDepartment(WeDepartment weDepartment);

    /**
     * 修改企业微信组织架构相关
     * 
     * @param weDepartment 企业微信组织架构相关
     * @return 结果
     */
    public int updateWeDepartment(WeDepartment weDepartment);

    /**
     * 删除企业微信组织架构相关
     * 
     * @param id 企业微信组织架构相关ID
     * @return 结果
     */
    public int deleteWeDepartmentById(Long id);

    /**
     * 批量删除企业微信组织架构相关
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeDepartmentByIds(Long[] ids);
}
