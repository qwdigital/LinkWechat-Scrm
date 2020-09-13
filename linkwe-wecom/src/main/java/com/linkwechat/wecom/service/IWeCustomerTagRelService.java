package com.linkwechat.wecom.service;

import java.util.List;
import com.linkwechat.wecom.domain.WeCustomerTagRel;

/**
 * 客户标签关系Service接口
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
public interface IWeCustomerTagRelService 
{
    /**
     * 查询客户标签关系
     * 
     * @param id 客户标签关系ID
     * @return 客户标签关系
     */
    public WeCustomerTagRel selectWeCustomerTagRelById(Long id);

    /**
     * 查询客户标签关系列表
     * 
     * @param weCustomerTagRel 客户标签关系
     * @return 客户标签关系集合
     */
    public List<WeCustomerTagRel> selectWeCustomerTagRelList(WeCustomerTagRel weCustomerTagRel);

    /**
     * 新增客户标签关系
     * 
     * @param weCustomerTagRel 客户标签关系
     * @return 结果
     */
    public int insertWeCustomerTagRel(WeCustomerTagRel weCustomerTagRel);

    /**
     * 修改客户标签关系
     * 
     * @param weCustomerTagRel 客户标签关系
     * @return 结果
     */
    public int updateWeCustomerTagRel(WeCustomerTagRel weCustomerTagRel);

    /**
     * 批量删除客户标签关系
     * 
     * @param ids 需要删除的客户标签关系ID
     * @return 结果
     */
    public int deleteWeCustomerTagRelByIds(Long[] ids);

    /**
     * 删除客户标签关系信息
     * 
     * @param id 客户标签关系ID
     * @return 结果
     */
    public int deleteWeCustomerTagRelById(Long id);
}
