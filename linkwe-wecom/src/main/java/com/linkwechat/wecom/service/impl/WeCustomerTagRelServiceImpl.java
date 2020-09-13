package com.linkwechat.wecom.service.impl;

import java.util.List;

import com.linkwechat.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeCustomerTagRelMapper;
import com.linkwechat.wecom.domain.WeCustomerTagRel;
import com.linkwechat.wecom.service.IWeCustomerTagRelService;

/**
 * 客户标签关系Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
@Service
public class WeCustomerTagRelServiceImpl implements IWeCustomerTagRelService 
{
    @Autowired
    private WeCustomerTagRelMapper weCustomerTagRelMapper;

    /**
     * 查询客户标签关系
     * 
     * @param id 客户标签关系ID
     * @return 客户标签关系
     */
    @Override
    public WeCustomerTagRel selectWeCustomerTagRelById(Long id)
    {
        return weCustomerTagRelMapper.selectWeCustomerTagRelById(id);
    }

    /**
     * 查询客户标签关系列表
     * 
     * @param weCustomerTagRel 客户标签关系
     * @return 客户标签关系
     */
    @Override
    public List<WeCustomerTagRel> selectWeCustomerTagRelList(WeCustomerTagRel weCustomerTagRel)
    {
        return weCustomerTagRelMapper.selectWeCustomerTagRelList(weCustomerTagRel);
    }

    /**
     * 新增客户标签关系
     * 
     * @param weCustomerTagRel 客户标签关系
     * @return 结果
     */
    @Override
    public int insertWeCustomerTagRel(WeCustomerTagRel weCustomerTagRel)
    {
        weCustomerTagRel.setCreateTime(DateUtils.getNowDate());
        return weCustomerTagRelMapper.insertWeCustomerTagRel(weCustomerTagRel);
    }

    /**
     * 修改客户标签关系
     * 
     * @param weCustomerTagRel 客户标签关系
     * @return 结果
     */
    @Override
    public int updateWeCustomerTagRel(WeCustomerTagRel weCustomerTagRel)
    {
        return weCustomerTagRelMapper.updateWeCustomerTagRel(weCustomerTagRel);
    }

    /**
     * 批量删除客户标签关系
     * 
     * @param ids 需要删除的客户标签关系ID
     * @return 结果
     */
    @Override
    public int deleteWeCustomerTagRelByIds(Long[] ids)
    {
        return weCustomerTagRelMapper.deleteWeCustomerTagRelByIds(ids);
    }

    /**
     * 删除客户标签关系信息
     * 
     * @param id 客户标签关系ID
     * @return 结果
     */
    @Override
    public int deleteWeCustomerTagRelById(Long id)
    {
        return weCustomerTagRelMapper.deleteWeCustomerTagRelById(id);
    }
}
