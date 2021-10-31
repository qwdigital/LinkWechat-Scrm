package com.linkwechat.wecom.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeFlowerCustomerTagRelMapper;
import com.linkwechat.wecom.domain.WeFlowerCustomerTagRel;
import com.linkwechat.wecom.service.IWeFlowerCustomerTagRelService;

/**
 * 客户标签关系Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-09-19
 */
@Service
public class WeFlowerCustomerTagRelServiceImpl extends ServiceImpl<WeFlowerCustomerTagRelMapper,WeFlowerCustomerTagRel> implements IWeFlowerCustomerTagRelService
{
    @Autowired
    private WeFlowerCustomerTagRelMapper weFlowerCustomerTagRelMapper;

    /**
     * 查询客户标签关系
     * 
     * @param id 客户标签关系ID
     * @return 客户标签关系
     */
    @Override
    public WeFlowerCustomerTagRel selectWeFlowerCustomerTagRelById(Long id)
    {
        return weFlowerCustomerTagRelMapper.selectWeFlowerCustomerTagRelById(id);
    }

    /**
     * 查询客户标签关系列表
     * 
     * @param weFlowerCustomerTagRel 客户标签关系
     * @return 客户标签关系
     */
    @Override
    public List<WeFlowerCustomerTagRel> selectWeFlowerCustomerTagRelList(WeFlowerCustomerTagRel weFlowerCustomerTagRel)
    {
        return weFlowerCustomerTagRelMapper.selectWeFlowerCustomerTagRelList(weFlowerCustomerTagRel);
    }

    /**
     * 新增客户标签关系
     * 
     * @param weFlowerCustomerTagRel 客户标签关系
     * @return 结果
     */
    @Override
    public int insertWeFlowerCustomerTagRel(WeFlowerCustomerTagRel weFlowerCustomerTagRel)
    {
        weFlowerCustomerTagRel.setCreateTime(DateUtils.getNowDate());
        return weFlowerCustomerTagRelMapper.insertWeFlowerCustomerTagRel(weFlowerCustomerTagRel);
    }

    /**
     * 修改客户标签关系
     * 
     * @param weFlowerCustomerTagRel 客户标签关系
     * @return 结果
     */
    @Override
    public int updateWeFlowerCustomerTagRel(WeFlowerCustomerTagRel weFlowerCustomerTagRel)
    {
        return weFlowerCustomerTagRelMapper.updateWeFlowerCustomerTagRel(weFlowerCustomerTagRel);
    }

    /**
     * 批量删除客户标签关系
     * 
     * @param ids 需要删除的客户标签关系ID
     * @return 结果
     */
    @Override
    public int deleteWeFlowerCustomerTagRelByIds(Long[] ids)
    {
        return weFlowerCustomerTagRelMapper.deleteWeFlowerCustomerTagRelByIds(ids);
    }

    /**
     * 删除客户标签关系信息
     * 
     * @param id 客户标签关系ID
     * @return 结果
     */
    @Override
    public int deleteWeFlowerCustomerTagRelById(Long id)
    {
        return weFlowerCustomerTagRelMapper.deleteWeFlowerCustomerTagRelById(id);
    }


    /**
     * 批量插入
     * @param weFlowerCustomerTagRels
     * @return
     */
    @Override
    public int batchInsetWeFlowerCustomerTagRel(List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels) {
        return weFlowerCustomerTagRelMapper.batchInsetWeFlowerCustomerTagRel(weFlowerCustomerTagRels);
    }

    @Override
    public void batchAddOrUpdate(List<WeFlowerCustomerTagRel> tagRels) {
        this.baseMapper.batchAddOrUpdate(tagRels);
    }
}
